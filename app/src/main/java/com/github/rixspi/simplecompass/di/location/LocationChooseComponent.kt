package com.github.rixspi.simplecompass.di.compass

import com.github.rixspi.simplecompass.di.base.scope.FragmentScope
import com.github.rixspi.simplecompass.ui.location.LocationChooserFragment
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = [(LocationChooserModule::class)])
interface LocationChooseComponent {
    fun inject(locationChooserFragment: LocationChooserFragment): LocationChooserFragment
}