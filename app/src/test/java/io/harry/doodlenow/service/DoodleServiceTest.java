package io.harry.doodlenow.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import io.harry.doodlenow.BuildConfig;
import io.harry.doodlenow.api.DoodleApi;
import io.harry.doodlenow.model.cloudant.CloudantDocument;
import io.harry.doodlenow.model.cloudant.CloudantResponse;
import io.harry.doodlenow.model.Doodle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DoodleServiceTest {
    private DoodleService subject;

    @Mock
    DoodleApi mockDoodleApi;
    @Mock
    Call<CloudantResponse<Doodle>> mockCloudantCall;
    @Mock
    ServiceCallback<List<Doodle>> mockServiceCallback;
    @Captor
    ArgumentCaptor<Callback<CloudantResponse<Doodle>>> cloudantCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockDoodleApi = mock(DoodleApi.class);
        subject = new DoodleService(mockDoodleApi);

        when(mockDoodleApi.getAllDoodles()).thenReturn(mockCloudantCall);
    }

    @Test
    public void getContents_callsContentApiToGetAllDoodles() throws Exception {
        subject.getDoodles(mockServiceCallback);

        verify(mockCloudantCall).enqueue(Matchers.<Callback<CloudantResponse<Doodle>>>any());
    }

    @Test
    public void afterGettingContents_runsSuccessServiceCallback() throws Exception {
        subject.getDoodles(mockServiceCallback);

        verify(mockCloudantCall).enqueue(cloudantCallbackCaptor.capture());

        CloudantResponse<Doodle> body = new CloudantResponse<>();
        CloudantDocument<Doodle> first = createDoodleDocument("first id", "first title", "first content");
        CloudantDocument<Doodle> second = createDoodleDocument("second id", "second title", "second content");

        body.rows = Arrays.asList(first, second);

        Response<CloudantResponse<Doodle>> response = Response.success(body);
        cloudantCallbackCaptor.getValue().onResponse(mockCloudantCall, response);

        verify(mockServiceCallback).onSuccess(Arrays.asList(
                createDoodleDocument("first id", "first title", "first content").doc,
                createDoodleDocument("second id", "second title", "second content").doc
        ));
    }

    private CloudantDocument<Doodle> createDoodleDocument(String id, String title, String content) {
        CloudantDocument<Doodle> document = new CloudantDocument<>();
        document.doc = new Doodle();
        document.doc._id = id;
        document.doc.title = title;
        document.doc.content = content;

        return document;
    }
}