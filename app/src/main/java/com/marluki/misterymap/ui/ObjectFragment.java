package com.marluki.misterymap.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marluki.misterymap.R;
import com.marluki.misterymap.model.ObjetoMapa;
import com.marluki.misterymap.provider.DatuBaseKontratua;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ObjectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ObjectFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView txt;
    private static String id;
    Cursor c;
    ObjetoMapa objetoMapa;
    String creatorUser;
    String cretorImage;

    public ObjectFragment() {
        // Required empty public constructor
    }

    public static ObjectFragment newInstance() {return new ObjectFragment();}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_object, container, false);

        //Nuevos parametros para el view del fragmento
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //Nueva Regla: EL fragmento estara debajo del boton add_fragment
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        //Margenes: top:41dp
        params.setMargins(0,100,0,0);
        //Setear los parametros al view
        view.setLayoutParams(params);

        txt = (TextView) view.findViewById(R.id.txtblank);
        Bundle bundle = getArguments();

        if(bundle!=null) {
            id = bundle.getString("id");
            if(id!=null)
                c = getActivity().getContentResolver().query(DatuBaseKontratua.Objetos_mapa.crearUriObjetoMapa(id), null, null, null, null);
            if (c.moveToFirst()) {
                objetoMapa = new ObjetoMapa();
                objetoMapa.setId(c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.ID)));
                objetoMapa.setDetalles(c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.DETALLES)));
                objetoMapa.setTipo_id(c.getInt(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.TIPO_ID)));
                objetoMapa.setPais(c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.PAIS)));
                objetoMapa.setCiudad(c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.CIUDAD)));
                objetoMapa.setNombre_objeto(c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO)));
                objetoMapa.setLatitud(c.getDouble(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LATITUD)));
                objetoMapa.setLongitud(c.getDouble(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LONGITUD)));
                creatorUser = c.getString(c.getColumnIndex(DatuBaseKontratua.Usuarios.NOMBRE));
                cretorImage = c.getString(c.getColumnIndex(DatuBaseKontratua.Usuarios.FOTO));
            }
               txt.setText(objetoMapa.getNombre_objeto());
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetail();
            }
        });

        return view;
    }

    private void goToDetail() {
    Intent intent=new Intent(getContext(), DetallesActivity.class);
        intent.putExtra("objeto", objetoMapa);
        intent.putExtra(DatuBaseKontratua.Usuarios.NOMBRE, creatorUser);
        intent.putExtra(DatuBaseKontratua.Usuarios.FOTO, cretorImage);
        startActivity(intent);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnComentInsertListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
