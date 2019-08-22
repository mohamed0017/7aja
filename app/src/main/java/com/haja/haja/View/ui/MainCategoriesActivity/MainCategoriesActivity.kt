package com.haja.haja.View.ui.MainCategoriesActivity

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.haja.haja.R
import com.haja.haja.Utils.ApplicationLanguageHelper
import com.haja.haja.Utils.inTransaction
import com.haja.haja.View.ui.AboutusScreen.AboutFragment
import com.haja.haja.View.ui.AddProduct.AddProductFragment
import com.haja.haja.View.ui.LoginScreen.LoginFragment
import com.haja.haja.View.ui.MainCategoriesScreen.MainCategoriesFragment
import com.haja.haja.View.ui.MyFavorites.FavoritesFragment
import com.haja.haja.View.ui.OffersScreen.OffersFragment
import com.haja.haja.View.ui.Register.RegisterFragment
import kotlinx.android.synthetic.main.activity_main_categories.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainCategoriesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigationOffers -> {
                supportFragmentManager.inTransaction {
                    replace(R.id.mainContainer, OffersFragment.newInstance()).addToBackStack("OffersFragment")
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationMessages -> {
                supportFragmentManager.inTransaction {
                    replace(R.id.mainContainer, AddProductFragment.newInstance()).addToBackStack("AddProductFragment")
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationMore -> {
                supportFragmentManager.inTransaction {
                    replace(R.id.mainContainer, RegisterFragment.newInstance()).addToBackStack("LoginFragment")
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationHome -> {
                supportFragmentManager.inTransaction {
                    replace(R.id.mainContainer, MainCategoriesFragment.newInstance()).addToBackStack("MainCategoriesFragment")
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationNotifications -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_categories)

        this.bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        nav_view.setNavigationItemSelectedListener(this)

        supportFragmentManager.inTransaction {
            replace(R.id.mainContainer, MainCategoriesFragment.newInstance())
        }
        categoriesBarMenu.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_main_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {

            }
            R.id.nav_lang -> {

            }
            R.id.nav_logOut -> {

            }
            R.id.nav_profile -> {
                supportFragmentManager.inTransaction {
                    replace(R.id.mainContainer, FavoritesFragment.newInstance()).addToBackStack("FavoritesFragment")
                }
            }
            R.id.nav_about -> {
                supportFragmentManager.inTransaction {
                    replace(R.id.mainContainer, AboutFragment.newInstance(1)).addToBackStack("staticContent")
                }
            }
            R.id.nav_policy -> {
                supportFragmentManager.inTransaction {
                    replace(R.id.mainContainer, AboutFragment.newInstance(2)).addToBackStack("staticContent")
                }
            }
            R.id.nav_conditions -> {
                supportFragmentManager.inTransaction {
                    replace(R.id.mainContainer, AboutFragment.newInstance(3)).addToBackStack("staticContent")
                }
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ApplicationLanguageHelper.wrap(newBase!!, "ar"))
    }
}
