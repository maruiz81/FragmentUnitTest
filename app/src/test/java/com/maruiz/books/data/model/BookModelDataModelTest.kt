package com.maruiz.books.data.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.maruiz.books.data.di.dataModule
import com.squareup.moshi.Moshi
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class BookModelDataModelTest : AutoCloseKoinTest() {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val moshi: Moshi by inject()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        startKoin {
            modules(
                listOf(
                    dataModule
                )
            )
        }
    }

    @Test
    fun `check the 'BookModel' is decoded properly`() {
        moshi.adapter(BookModelDataModel::class.java).fromJson(bookJson)!!.run {
            bookId shouldEqual 1
            title shouldEqual "Anna Karenina"
            author shouldEqual "Leo Tolstoy"
            shortSynopsis shouldEqual "Anna Karenina is a novel by the Russian author Leo Tolstoy, " +
                    "first published in book form in 1878. Many authors consider Anna Karenina the " +
                    "greatest work of literature ever written, and Tolstoy himself called it his first true novel."
            synopsis shouldEqual "Any fan of stories that involve juicy subjects like adultery, " +
                    "gambling, marriage plots, and, well, Russian feudalism, would instantly place " +
                    "Anna Karenina at the peak of their “greatest novels” list. And that’s exactly " +
                    "the ranking that publications like Time magazine have given the novel since it " +
                    "was published in its entirety in 1878. Written by Russian novelist Leo Tolstoy, " +
                    "the eight-part towering work of fiction tells the story of two major characters: " +
                    "a tragic, disenchanted housewife, the titular Anna, who runs off with her young " +
                    "lover, and a lovestruck landowner named Konstantin Levin, who struggles in faith " +
                    "and philosophy. Tolstoy molds together thoughtful discussions on love, pain, and " +
                    "family in Russian society with a sizable cast of characters regarded for their " +
                    "realistic humanity. The novel was especially revolutionary in its treatment of " +
                    "women, depicting prejudices and social hardships of the time with vivid emotion."
            image shouldEqual "https://raw.githubusercontent.com/maruiz81/service-example/develop/BooksApp/books_images/anna_karenina.jpeg"
        }
    }

    @Test
    fun `check the 'BookModel' without author`() {
        moshi.adapter(BookModelDataModel::class.java).fromJson(emptyJson)!!.run {
            bookId shouldEqual 0
            title shouldEqual ""
            author shouldEqual ""
            shortSynopsis shouldEqual ""
            synopsis shouldEqual ""
            image shouldEqual ""
        }
    }

    companion object {
        private const val bookJson = "{\n" +
                "    \"bookId\":1,\n" +
                "    \"title\":\"Anna Karenina\",\n" +
                "    \"author\":\"Leo Tolstoy\",\n" +
                "    \"short_synopsis\":\"Anna Karenina is a novel by the Russian author Leo Tolstoy, " +
                "first published in book form in 1878. Many authors consider Anna Karenina the greatest " +
                "work of literature ever written, and Tolstoy himself called it his first true novel.\",\n" +
                "    \"synopsis\":\"Any fan of stories that involve juicy subjects like adultery, gambling, " +
                "marriage plots, and, well, Russian feudalism, would instantly place Anna Karenina at the peak " +
                "of their “greatest novels” list. And that’s exactly the ranking that publications like Time " +
                "magazine have given the novel since it was published in its entirety in 1878. Written by Russian " +
                "novelist Leo Tolstoy, the eight-part towering work of fiction tells the story of two major " +
                "characters: a tragic, disenchanted housewife, the titular Anna, who runs off with her young lover, " +
                "and a lovestruck landowner named Konstantin Levin, who struggles in faith and philosophy. Tolstoy " +
                "molds together thoughtful discussions on love, pain, and family in Russian society with a sizable " +
                "cast of characters regarded for their realistic humanity. The novel was especially revolutionary " +
                "in its treatment of women, depicting prejudices and social hardships of the time with vivid emotion.\",\n" +
                "    \"image\":\"https://raw.githubusercontent.com/maruiz81/service-example/develop/BooksApp/books_images/anna_karenina.jpeg\"\n" +
                "  }"
    }

    private val emptyJson = "{}"
}