package com.example.famdif_final;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class BusquedaFragment extends BaseFragment {

    private TipoTienda tipoTiendaLista;
    private SubtipoTienda subtipoTiendaLista;

    private Spinner tipoTienda;
    private Spinner subtipoTienda;
    private Spinner despDistancia;
    private Spinner despAccesibilidad;

    private ArrayAdapter adapt;
    private ArrayAdapter adapt1;
    private ArrayAdapter adapt2;
    private ArrayAdapter adapt3;

    private String tTienda;
    private String stTienda;
    private String distSeleccionada;
    private String acceSeleccionada;

    private TextView nombreTienda;
    private TextView direccion;
    private List<String> dist= Arrays.asList("100","200","500","1000","Cualquiera");
    private List<String> accesibilidad=Arrays.asList("ACCESIBLE", "ACCESIBLE CON DIFICULTAD","PRACTICABLE CON AYUDA","CUALQUIERA");
    private Button busqueda;
    private DatabaseReference resultado;




    public BusquedaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setMainActivity((MainActivity) getActivity());
        final View view=inflater.inflate(R.layout.fragment_busqueda, container, false);

        tipoTienda=view.findViewById(R.id.desplegableTipoEstablecimiento);
        subtipoTienda=view.findViewById(R.id.desplegablesubtipoEstablecimiento);
        despDistancia=view.findViewById(R.id.desplegableDistancia);
        despAccesibilidad=view.findViewById(R.id.desplegableAccesibilidad);
        busqueda=view.findViewById(R.id.btnBusqueda);
        nombreTienda=view.findViewById(R.id.textoNombreTienda);
        direccion=view.findViewById(R.id.textoDireccion);


        tipoTiendaLista = new TipoTienda();
        subtipoTiendaLista = new SubtipoTienda("Alimentacion");

        adapt = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,tipoTiendaLista.getTiposTienda());
        tipoTienda.setAdapter(adapt);


        adapt1 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,subtipoTiendaLista.getSubTiposTienda());
        subtipoTienda.setAdapter(adapt1);

        adapt2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, dist);
        despDistancia.setAdapter(adapt2);

        adapt3 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,accesibilidad);
        despAccesibilidad.setAdapter(adapt3);

        tipoTienda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tTienda = (String) tipoTienda.getAdapter().getItem(position);
                SubtipoTienda subtTienda = new SubtipoTienda(tTienda);
                ArrayAdapter adapt4 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,subtTienda.getSubTiposTienda());
                subtipoTienda.setAdapter(adapt4);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subtipoTienda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stTienda = (String) subtipoTienda.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        despAccesibilidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                acceSeleccionada = (String) despAccesibilidad.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        despDistancia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                distSeleccionada = (String) despDistancia.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        busqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((!tTienda.matches("Cualquiera")) && ( nombreTienda.getText().toString().isEmpty() || direccion.getText().toString().isEmpty()))
                    Toast.makeText(getContext(), "El nombre y la dirección no pueden ser vacías",Toast.LENGTH_SHORT).show();
                else{

                    Query q=MainActivity.databaseReference.child("shops").orderByChild("nombre").equalTo(nombreTienda.getText().toString());

                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dato: snapshot.getChildren()){
                                String name= dato.child("nombre").getValue().toString();
                                Log.i("DATO", name);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    //Toast.makeText(getContext(), "Iniciamos la busqueda",Toast.LENGTH_SHORT).show();
                    //getMainActivity().getSupportActionBar().setTitle("RESULTADOS");
                    //getMainActivity().clearBackStack();
                    //getMainActivity().setFragment(FragmentName.MAP);
                }
            }
        });

        getMainActivity().getSupportActionBar().setTitle("BUSQUEDA");
        getMainActivity().changeMenu(MenuType.USER_LOGGED);
        getMainActivity().setOptionMenu(R.id.item_search);
        return view;
    }


}