import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Left
import arrow.core.Right
import com.maruiz.books.data.di.dataModule
import com.maruiz.books.data.error.Failure
import com.maruiz.books.data.getBookListData
import com.maruiz.books.data.model.BookModelDataModel
import com.maruiz.books.data.repository.BookRepository
import com.maruiz.books.data.services.BookApi
import com.maruiz.books.domain.di.domainModule
import com.maruiz.books.presentation.di.presentationModule
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.Times
import retrofit2.Call
import retrofit2.mock.Calls

@RunWith(JUnit4::class)
class BookRepositoryTest : AutoCloseKoinTest() {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var bookApi: BookApi

    private val bookRepository: BookRepository by inject()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        startKoin {
            modules(
                listOf(
                    dataModule,
                    module(override = true) {
                        single { bookApi }
                    }
                ))
        }
    }

    @Test
    fun `should return an empty list`() {
        val response: Call<List<BookModelDataModel>> = Calls.response(emptyList())
        whenever(bookApi.getBooks()).thenReturn(response)
        runBlocking { bookRepository.getBooks() shouldEqual Right(emptyList()) }

        verify(bookApi, Times(1)).getBooks()
    }

    @Test
    fun `should return a 10 items list`() {
        val bookList = getBookListData(10)
        val response: Call<List<BookModelDataModel>> = Calls.response(bookList)
        whenever(bookApi.getBooks()).thenReturn(response)

        runBlocking { bookRepository.getBooks() shouldEqual Right(bookList) }

        verify(bookApi, Times(1)).getBooks()
    }

    @Test
    fun `should return a failure`() {
        val response = Calls.failure<List<BookModelDataModel>>(Throwable())
        whenever(bookApi.getBooks()).thenReturn(response)

        runBlocking { bookRepository.getBooks() shouldEqual Left(Failure.GenericError) }

        verify(bookApi, Times(1)).getBooks()
    }
}