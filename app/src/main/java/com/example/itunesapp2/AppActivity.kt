package com.example.itunesapp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.viewModelScope
import com.example.itunesapp2.di.DI
import com.example.itunesapp2.navigation.Screens
import com.example.itunesapp2.view.BaseFragment
import kotlinx.coroutines.isActive
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import timber.log.Timber
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.viewmodel.installViewModelBinding

class AppActivity : AppCompatActivity() {

    val router: Router by inject<Router>()
    val navigatorHolder: NavigatorHolder by inject<NavigatorHolder>()

    private val currentFragment: BaseFragment?
        get() = supportFragmentManager.findFragmentById(R.id.container) as? BaseFragment

    private val navigator: Navigator = object: SupportAppNavigator(this, supportFragmentManager, R.id.container) {
        override fun setupFragmentTransaction(
            command: Command?,
            currentFragment: Fragment?,
            nextFragment: Fragment?,
            fragmentTransaction: FragmentTransaction
        ) {
            fragmentTransaction.setReorderingAllowed(true)
        }

        override fun applyCommands(commands: Array<out Command>?) {
            if (Thread.currentThread() != Looper.getMainLooper().thread) {
                runOnUiThread {
                    super.applyCommands(commands)
                }
            } else {
                super.applyCommands(commands)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependencies()
        router.newRootScreen(Screens.MainScreen)
    }

    private fun injectDependencies() {
        KTP.openScope(DI.APP_SCOPE)
            .inject(this)
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag("MYTAG").i("onDestroy")
    }
}