package k.s.task2banaoapp.dagger;

import androidx.fragment.app.Fragment;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.migration.DisableInstallInCheck;

@Module
@DisableInstallInCheck
public class AppModule {
    private Fragment mFragment;
    @Inject
    public AppModule(Fragment mFragment) {
        this.mFragment = mFragment;
    }

    @Provides
    @Singleton
    Fragment provideFragment() {
        return mFragment;
    }
}
