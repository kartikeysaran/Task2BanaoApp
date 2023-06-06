package k.s.task2banaoapp.dagger;

import javax.inject.Singleton;

import dagger.Component;
import k.s.task2banaoapp.ui.home.HomeFragment;
import k.s.task2banaoapp.ui.search.SearchFragment;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(HomeFragment homeFragment);
    void inject(SearchFragment searchFragment);

}
