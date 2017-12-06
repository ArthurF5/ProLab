package com.moliveiralucas.prolab;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.moliveiralucas.prolab.Controle.Apagar;
import com.moliveiralucas.prolab.Controle.Atualizar;
import com.moliveiralucas.prolab.Controle.BuscaExames;
import com.moliveiralucas.prolab.Controle.Cadastro;
import com.moliveiralucas.prolab.Controle.Login;
import com.moliveiralucas.prolab.Controle.MapsActivity;
import com.moliveiralucas.prolab.Controle.NewAcc;
import com.moliveiralucas.prolab.Controle.Preferencias;
import com.moliveiralucas.prolab.model.Usuario;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BuscaExames.DataPassListener, Login.EfetuarLogin{

    private FragmentManager fragmentManager;
    private Usuario usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.login:
                showFragment(new Login(), "Login");
                break;
            case R.id.nova_conta:
                showFragment(new NewAcc(), "NewAcc");
                break;
            case R.id.preferencias:
                showFragment(new Preferencias(), "Preferencias");
                break;
            case R.id.create:
                showFragment(new Cadastro(), "Cadastro");
                break;
            case R.id.update:
                showFragment(new Atualizar(), "Atualizar");
                break;
            case R.id.delete:
                showFragment(new Apagar(), "Apagar");
                break;
            case R.id.logout:
                usr = null;
                showFragment(new Login(), "Login");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem login = menu.findItem(R.id.login);
        MenuItem logout = menu.findItem(R.id.logout);
        MenuItem create = menu.findItem(R.id.create);
        MenuItem update = menu.findItem(R.id.update);
        MenuItem delete = menu.findItem(R.id.delete);

        MenuItem novaConta = menu.findItem(R.id.nova_conta);
        MenuItem preferencias = menu.findItem(R.id.preferencias);
        if (usr != null) {
            /*Usuario Administrador*/
            if (usr.getPermissao() == 0) {
                login.setVisible(false);
                logout.setVisible(true);
                update.setVisible(true);
                delete.setVisible(true);
                create.setVisible(true);
                novaConta.setVisible(false);
            } else if (usr.getPermissao() == 1) {/*Usuario Comum*/
                login.setVisible(false);
                logout.setVisible(true);
                novaConta.setVisible(false);
                preferencias.setVisible(true);
            }
        } else {
            login.setVisible(true);
            novaConta.setVisible(true);
            logout.setVisible(false);
            preferencias.setVisible(false);
            create.setVisible(false);
            update.setVisible(false);
            delete.setVisible(false);
        }
        return true;
    }

    private void showFragment(Fragment fragment, String name) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.idContainer, fragment, name);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_buscarexame:
                showFragment(new BuscaExames(), "BuscaExames");
                break;
            case R.id.nav_sobre:

                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void passAddress(String address) {
        MapsActivity mapsActivity = new MapsActivity();
        Bundle args = new Bundle();
        args.putString(mapsActivity.DATA_RECEIVE, address);
        mapsActivity.setArguments(args);
        showFragment(mapsActivity, "MapsActivity");
    }

    @Override
    public void efetuarLogin(Usuario usr) {
        this.usr = usr;
    }
}
