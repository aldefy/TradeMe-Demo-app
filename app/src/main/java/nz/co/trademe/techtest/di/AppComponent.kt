package nz.co.trademe.techtest.di

import dagger.Component
import nz.co.trademe.techtest.TradeMeApplication
import nz.co.trademe.wrapper.di.WrapperComponent
import nz.co.trademe.wrapper.di.scope.ApplicationScope

@ApplicationScope
@Component(
    modules = [(AppModule::class)],
    dependencies = [(WrapperComponent::class)]
)
interface AppComponent {
      fun inject(app: TradeMeApplication)
}
