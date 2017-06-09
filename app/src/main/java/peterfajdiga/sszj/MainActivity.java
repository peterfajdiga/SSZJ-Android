package peterfajdiga.sszj;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import peterfajdiga.sszj.elements.adapters.OnWordClickedListener;
import peterfajdiga.sszj.elements.adapters.SetsAdapter;
import peterfajdiga.sszj.logic.pojo.Set;
import peterfajdiga.sszj.logic.Words;
import peterfajdiga.sszj.logic.requests.Constants;
import peterfajdiga.sszj.sections.AboutFragment;
import peterfajdiga.sszj.sections.SectionFragment;
import peterfajdiga.sszj.sections.SetFragment;
import peterfajdiga.sszj.sections.SetsFragment;
import peterfajdiga.sszj.sections.SpellingFragment;
import peterfajdiga.sszj.sections.WordFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SectionFragment.OnFragmentInteractionListener,
        OnWordClickedListener,
        SetsAdapter.OnSetClickedListener {

    private MenuItem searchViewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        final Intent intent = getIntent();
        switch (intent.getAction()) {
            case Intent.ACTION_MAIN: {
                // show SetsFragment by default
                loadSectionFragment(new SetsFragment(), false);
                break;
            }
            case Intent.ACTION_SEARCH: {
                final String query = intent.getStringExtra(SearchManager.QUERY);
                if (Words.isValidWord(query)) {
                    loadWord(query, false);
                    new SearchRecentSuggestions(this, SearchRecentProvider.AUTHORITY, SearchRecentProvider.MODE).saveRecentQuery(query, null);  // save search
                } else if (Words.isValidWordSpelling(query)) {
                    final Fragment fragment = new SpellingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(WordFragment.BUNDLE_KEY_WORD, query);
                    fragment.setArguments(bundle);
                    loadSectionFragment(fragment, false);
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // normal back
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView)searchViewItem.getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_sets:     loadSectionFragment(new SetsFragment()); break;
            case R.id.nav_search:   searchViewItem.expandActionView(); break;
            case R.id.nav_spelling: loadSectionFragment(new SpellingFragment()); break;
            case R.id.nav_practice: Constants.openWebsite(this, "http://sszj.fri.uni-lj.si/?stran=vaje.index"); break;
            case R.id.nav_about:    loadSectionFragment(new AboutFragment()); break;
        }

        // Close the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadSectionFragment(final Fragment fragment, final boolean saveStack) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction().replace(R.id.content_frame, fragment);
        if (saveStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
    private void loadSectionFragment(final Fragment fragment) {
        loadSectionFragment(fragment, true);
    }

    private void loadWord(String word, boolean saveStack) {
        final Fragment fragment = new WordFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WordFragment.BUNDLE_KEY_WORD, word);
        fragment.setArguments(bundle);
        loadSectionFragment(fragment, saveStack);
    }

    @Override
    public void onSetTitle(String title) {
        setTitle(title);
    }

    @Override
    public void onWordClicked(String word) {
        loadWord(word, true);
    }

    @Override
    public void onSetClicked(Set set) {
        final Fragment fragment = new SetFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SetFragment.BUNDLE_KEY_SET, set);
        fragment.setArguments(bundle);
        loadSectionFragment(fragment, true);
    }
}
