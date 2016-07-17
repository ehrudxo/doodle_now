package io.harry.doodlenow.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.api.DoodleApi;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.view.DoodleIcon;

import static org.mockito.Mockito.mock;

@Module
public class TestDoodleModule {
    @Provides @Singleton
    public DoodleService provideDoodleService() {
        return mock(DoodleService.class);
    }

    @Provides @Singleton
    public DoodleApi provideDoodleApi() {
        return mock(DoodleApi.class);
    }

    @Provides @Singleton
    public DoodleIcon provideDoodleIcon() {
        return mock(DoodleIcon.class);
    }

    @Provides @Singleton
    public DoodleListAdapter provideDoodleListAdapter() { return mock(DoodleListAdapter.class); }
}