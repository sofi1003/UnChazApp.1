package com.example.unchazapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unchazapp.R;
import com.example.unchazapp.acces.GenericDAO;
import com.example.unchazapp.acces.KeyCallback;
import com.example.unchazapp.acces.NegocioCallback;
import com.example.unchazapp.model.Catalogo;
import com.example.unchazapp.model.Negocio;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MapaGeneral extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, SearchView.OnQueryTextListener {

    private GoogleMap mMap;
    private ListView ListViewContacto;

    private TextView nombreNeg;
    private TextView categoriaNeg;

    private final int FINE_PERMISSION = 1;
    private Location currentLocation;
    private FusedLocationProviderClient fLPC;
    private List<Negocio> lst;
    private SearchView barraBuscadora;

    private CustomAdapter adapter;

    private ImageView imagenBarra;
    private String nombre;
    private String categoria;
    private String key;
    private Double latitud;
    private Double longitud;
    private Marker negocioMarker;
    private LinearLayout barra;
    private View mapaa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_general);
        ListViewContacto = findViewById(R.id.ListViewContactoMapa);
        barraBuscadora = findViewById(R.id.txtBuscarNegocioMapa);
        barra = findViewById(R.id.barra);
        mapaa = findViewById(R.id.map);
        nombreNeg = findViewById(R.id.TituloBarra);
        categoriaNeg = findViewById(R.id.CategoriaBarra);
        imagenBarra = findViewById(R.id.imagenMapaGeneralBarra);

        loadNegociosData(new NegocioCallback() {
            @Override
            public void onNegociosLoaded(List<Negocio> listaNegocios) {
                lst = listaNegocios;

                adapter = new CustomAdapter(MapaGeneral.this, lst);
                ListViewContacto.setAdapter(adapter);


            }
        });

        ListViewContacto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeyboard();
                ListViewContacto.setVisibility(View.GONE);
                Negocio c = lst.get(i);
                nombre = c.getNombreNegocio();

                categoria = c.getCategoriaNegocio().toString();

                latitud = c.getLatitud();
                longitud = c.getLongitud();
                addMarkerToMap(latitud, longitud, nombre);
                barra.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params = mapaa.getLayoutParams();
                params.height = 1500;
                mapaa.setLayoutParams(params);
                nombreNeg.setText(nombre);
                categoriaNeg.setText(categoria);

                if (c.getImagen().length()>10){
                    Picasso.get().load(c.getImagen()).into(imagenBarra);
                }else{
                    imagenBarra.setImageResource(R.drawable.imagensita);
                }





            }
        });

        barraBuscadora.setOnQueryTextListener(this);

        fLPC = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();


    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    private void loadNegociosData(NegocioCallback callback) {
        GenericDAO<Negocio> negocioDao = new GenericDAO<>();
        negocioDao.recoverAll("negocios", new KeyCallback() {
            @Override
            public void onKeyFound(Object key) {
                System.out.println(key);
                List<Negocio> listaNegocios = Negocio.toListNegocio((List<Map<String, Object>>) key);
                List<Negocio> lst = new ArrayList<>();

                for (Negocio negoc : listaNegocios) {
                    Negocio negocio = new Negocio(negoc.getNombreNegocio(), negoc.getDescripcionNegocio(), negoc.getImagen(), negoc.getCategoriaNegocio());
                    negocio.setLatitud(negoc.getLatitud());
                    negocio.setLongitud(negoc.getLongitud());
                    System.out.println("---------------------esto debería ir primero---------------");
                    System.out.println(negocio.toString());
                    lst.add(negocio);
                }

                // Llama al callback con la lista de negocios una vez que estén disponibles.
                callback.onNegociosLoaded(lst);
            }

            @Override
            public void onError(String errorMessage) {
                System.out.println(errorMessage);
            }
        });
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        System.out.println("LONGITUD DEL TEXTO " + s.length());
        if(s.length() == 0){
            ListViewContacto.setVisibility(View.GONE);
            System.out.println(" nulo");
            System.out.println(adapter.toString());
        }else{
            if (adapter != null) {
                System.out.println("no nulo");
                System.out.println(adapter.toString());
                adapter.filtrado(s);
                ListViewContacto.setVisibility(View.VISIBLE);
            }
        }


        return false;
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION);
            return;
        }
        Task<Location> task = fLPC.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapaGeneral.this);


                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(16);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                // Si deseas personalizar el contenido de la ventana completa, retorna null aquí
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Vista personalizada para el contenido de la ventana de información
                View infoView = getLayoutInflater().inflate(R.layout.custom_window_info, null);

                // Obtén referencias a los elementos de la vista personalizada
                TextView titleTextView = infoView.findViewById(R.id.infoWindowTitle);
                TextView snippetTextView = infoView.findViewById(R.id.infoWindowSnippet);

                // Establece el contenido de los elementos
                titleTextView.setText(marker.getTitle());
                snippetTextView.setText(marker.getSnippet());

                return infoView;
            }
        });

        LatLng miUbicacion = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());mMap.addMarker(new MarkerOptions()
                .position(miUbicacion)
                .title("Mi Locacion"));


        LatLngBounds zonaPermitida = new LatLngBounds(
                new LatLng(4.632037316971541, -74.08961060988099), // Esquina inferior izquierda
                new LatLng(4.643198230686855, -74.07868614743685)  // Esquina superior derecha
        );

        mMap.setLatLngBoundsForCameraTarget(zonaPermitida);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(zonaPermitida, 0));
       this.mMap.setOnMapClickListener(this);

    };
    private void addMarkerToMap(Double latitud, Double longitud, String nombreNegocio) {

        mMap.setMinZoomPreference(16);
        LatLngBounds zonaPermitida = new LatLngBounds(
                new LatLng(4.632037316971541, -74.08961060988099), // Esquina inferior izquierda
                new LatLng(4.643198230686855, -74.07868614743685)  // Esquina superior derecha
        );
        mMap.setLatLngBoundsForCameraTarget(zonaPermitida);
        if (negocioMarker != null) {
            negocioMarker.remove();
        }

        if (mMap != null) {
            LatLng negocioLatLng = new LatLng(latitud, longitud);

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(negocioLatLng)
                    .title(nombreNegocio)
                    .snippet("Descripción del negocio aquí");
            negocioMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(negocioLatLng));
        }
    }


    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        ListViewContacto.setVisibility(View.GONE);
        barra.setVisibility(View.GONE);
        ViewGroup.LayoutParams params = mapaa.getLayoutParams();
        params.height = 2000;
        mapaa.setLayoutParams(params);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else{
                Toast.makeText(this, "La locacion fue denagada, porfa aceptala", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void irAPerfilNegocio(View view){
        Intent i = new Intent(this, PerfilNegocio.class);
        Bundle bundle = new Bundle();
        bundle.putString("nombreNegocio", nombre);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void irACatalogo(View view){
        Intent i = new Intent(this, Catalogo.class);
        startActivity(i);
    }

    public void irAPedido(View view){
        Intent i = new Intent(this, Pedido.class);
        startActivity(i);
    }






}