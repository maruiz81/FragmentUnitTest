# Fragment Isolate Testing

This is a pet project to play with android 
[fragment-testing](https://developer.android.com/training/basics/fragments/testing) 
and [Robolectric](http://robolectric.org/). These libraries make possible to test fragments in isolation (without having to include then in an activity) and running the
test in a Robolectric framework which makes possible to mock all preconditions in order to
run the unit test separately. These libraries are combined with other as 
[Mokito](https://site.mockito.org/) and 
[Expresso](https://developer.android.com/training/testing/espresso)

When the app starts, loads a RecyclerView with a list of books. Them if the user clicks
in some of the books a "detail view" is opened where is possible to check the full
description of the books.