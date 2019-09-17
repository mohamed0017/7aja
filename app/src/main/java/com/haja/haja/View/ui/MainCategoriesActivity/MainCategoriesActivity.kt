package com.haja.haja.View.ui.MainCategoriesActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.haja.haja.R
import com.haja.haja.Utils.*
import com.haja.haja.View.ui.AboutusScreen.AboutFragment
import com.haja.haja.View.ui.AddProduct.AddProductFragment
import com.haja.haja.View.ui.LoginScreen.LoginFragment
import com.haja.haja.View.ui.MainCategoriesScreen.MainCategoriesFragment
import com.haja.haja.View.ui.MoreScreen.MoreFragment
import com.haja.haja.View.ui.MyFavorites.FavoritesFragment
import com.haja.haja.View.ui.NotificationsHistory.NotificationsListFragment
import com.haja.haja.View.ui.OffersScreen.OffersFragment
import com.haja.haja.View.ui.Register.RegisterFragment
import com.infovass.lawyerskw.lawyerskw.Utils.ui.SnackAndToastUtil.Companion.makeToast
import kotlinx.android.synthetic.main.activity_main_categories.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainCategoriesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

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
0
        supportFragmentManager.inTransaction {
            add(R.id.mainContainer, MainCategoriesFragment.newInstance())
        }
        categoriesBarMenu.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }
        categoriesBarBack.setOnClickListener {
            onBackPressed()
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


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigationHome -> {
                supportFragmentManager.popBackStackImmediate()
                supportFragmentManager.inTransaction {
                    replace(R.id.mainContainer, MainCategoriesFragment.newInstance())
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationOffers -> {
                supportFragmentManager.popBackStackImmediate()
                supportFragmentManager.inTransaction {
                    replace(R.id.mainContainer, OffersFragment.newInstance())
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationMessages -> {

                supportFragmentManager.popBackStackImmediate()
                val userId = SharedPreferenceUtil(this).getString(USERID, "0")?.toInt()
                if (userId == 0) {
                    makeToast(this, resources.getString(R.string.login_first))
                    supportFragmentManager.inTransaction {
                        replace(R.id.mainContainer, LoginFragment.newInstance())
                    }
                } else
                    supportFragmentManager.inTransaction {
                        replace(R.id.mainContainer, AddProductFragment.newInstance())
                    }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationNotifications -> {

                supportFragmentManager.popBackStackImmediate()
                val userId = SharedPreferenceUtil(this).getString(USERID, "0")?.toInt()
                if (userId == 0) {
                    makeToast(this, resources.getString(R.string.login_first))
                    supportFragmentManager.inTransaction {
                        replace(R.id.mainContainer, LoginFragment.newInstance())
                    }
                } else
                    supportFragmentManager.inTransaction {
                        replace(
                            R.id.mainContainer
                            , NotificationsListFragment.newInstance()
                        )
                    }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationMore -> {

                supportFragmentManager.inTransaction {
                    replace(R.id.mainContainer, MoreFragment.newInstance())
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {

            }
            R.id.nav_lang -> {
                val lang = SharedPreferenceUtil(this).getString(LANG, "ar")
                if (lang == "ar")
                    SharedPreferenceUtil(this).putString(LANG, "en")
                else
                    SharedPreferenceUtil(this).putString(LANG, "ar")
                startActivity(Intent(this, MainCategoriesActivity::class.java))
                finish()
            }
            R.id.nav_logOut -> {
                supportFragmentManager.popBackStackImmediate()
                SharedPreferenceUtil(this).putString(TOKEN, "")
                SharedPreferenceUtil(this).putString(USERID, "0")
                supportFragmentManager.inTransaction {
                    replace(R.id.mainContainer, LoginFragment.newInstance())
                }
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
        val lang = SharedPreferenceUtil(newBase!!).getString(LANG, "ar")
        super.attachBaseContext(ApplicationLanguageHelper.wrap(newBase, "$lang"))
    }
}
