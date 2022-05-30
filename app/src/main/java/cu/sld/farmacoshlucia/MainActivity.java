package cu.sld.farmacoshlucia;

import static retrofit2.converter.gson.GsonConverterFactory.create;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.sld.farmacoshlucia.R;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    String NetworkAlert="¿Usted esta conectado?";
    public static final String BASE_URL = "http://www.hcqho.sld.cu";
    private RecyclerView mRecyclerView;
    private ArrayList<AndroidVersion> mArrayList;
    private DataAdapter mAdapter;
    private ProgressBar progress;
    AlertDialog.Builder altDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.Theme_DeviceDefault);
        setContentView(R.layout.activity_main);
        //AutoUpdate
        new AppUpdater(this)
                .setUpdateFrom(UpdateFrom.JSON)
                .setUpdateJSON("http://www.hcqho.sld.cu/pub/apk/farmacos_app/farmacos_update.json")
                //.setUpdateJSON("http://181.225.255.41/farmacos.json")
                .setIcon(R.drawable.update)
                .showEvery(3)
                .showAppUpdated(true)
                //.setDisplay(Display.NOTIFICATION)
                //.setDisplay(Display.SNACKBAR)
                .setDisplay(Display.DIALOG)
                .start();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        altDialog = new AlertDialog.Builder(this);
        altDialog.setCancelable(false);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        initViews();
        loadJSON();
    }

    private void initViews(){
        Networkcheck activeNetwork = new Networkcheck(this);
        boolean isConnected = activeNetwork.isConnectingToInternet();
        System.out.println("Conexión : " + isConnected); // Network Check
        if (isConnected == true) {
        mRecyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        } else {
            Alert(NetworkAlert);
        }
    }
    private void loadJSON(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(create())
                .build();

        RequestInterface request = retrofit.create(RequestInterface.class);

        Call<JSONResponse> call = request.getJSON();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                progress.setVisibility(View.GONE);
                JSONResponse jsonResponse = response.body();
                mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getAndroid()));
                mAdapter = new DataAdapter(mArrayList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }

        });
    }
    public  void Alert(String Alert){
        //progress.setVisibility(View.GONE);
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        alertDialog.setIcon(R.drawable.wifi);
        alertDialog.setTitle("¡Alerta!");
        alertDialog.setMessage(Alert);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cerrar",
                (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Reintentar",
                (dialog, which) -> {
                    initViews();
                    loadJSON();
                    dialog.dismiss();
                });
        alertDialog.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Buscar Medicamentos");
        searchView.setIconified(false);
        search(searchView);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (mAdapter != null) mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
