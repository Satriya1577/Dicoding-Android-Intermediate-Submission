package com.example.mystoryapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.map
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.mystoryapp.DataDummy
import com.example.mystoryapp.MainDispatcherRule
import com.example.mystoryapp.adapter.StoryAdapter
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.data.local.Story
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.getOrAwaitValue
import com.example.mystoryapp.ui.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()
    @Mock
    private lateinit var storyRepository: StoryRepository

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(storyRepository)
        val actualStory: PagingData<Story> = mainViewModel.stories.getOrAwaitValue()

        val pagingDataStory: PagingData<Story> = actualStory
        val newActualStory: PagingData<ListStoryItem> = pagingDataStory.map { story ->
            ListStoryItem(
                photoUrl = story.photoUrl,
                createdAt = story.createdAt,
                name = story.name,
                description = story.description,
                lon = story.lon,
                id = story.id,
                lat = story.lat
            )
        }

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(newActualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], toStory(differ.snapshot()[0]))
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(storyRepository)
        val actualStory: PagingData<Story> = mainViewModel.stories.getOrAwaitValue()

        val pagingDataStory: PagingData<Story> = actualStory
        val newActualStory: PagingData<ListStoryItem> = pagingDataStory.map { story ->
            ListStoryItem(
                photoUrl = story.photoUrl,
                createdAt = story.createdAt,
                name = story.name,
                description = story.description,
                lon = story.lon,
                id = story.id,
                lat = story.lat
            )
        }

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(newActualStory)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

private fun toStory(listStoryItem: ListStoryItem?) : Story {
    return Story (
        photoUrl = listStoryItem?.photoUrl,
        createdAt = listStoryItem?.createdAt,
        name = listStoryItem?.name,
        description = listStoryItem?.description,
        lon = listStoryItem?.lon,
        id = listStoryItem?.id ?: "",
        lat = listStoryItem?.lat
    )
}

class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
