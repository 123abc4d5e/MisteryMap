package com.marluki.misterymap.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marluki.misterymap.ObjetosAdapter;
import com.marluki.misterymap.R;
import com.marluki.misterymap.model.ObjetoMapa;
import com.marluki.misterymap.provider.DatuBaseKontratua;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FragmentList extends Fragment {

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private ObjetosAdapter adapter;
    private ContentResolver resolver;
    private ObjetoMapa objetoMapa;

    public FragmentList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle bundle=getArguments();

        int tipo=bundle.getInt(DatuBaseKontratua.Objetos_mapa.TIPO_ID);
        //TODO Conseguir un id

        resolver=getContext().getContentResolver();

        Uri uri = DatuBaseKontratua.Objetos_mapa.URI_CONTENT;
        String selection = DatuBaseKontratua.Objetos_mapa.TIPO_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(tipo)};

        Cursor c=resolver.query(uri,null,selection,selectionArgs,null);
        ArrayList<ObjetoMapa> ObjetoMapaArrayList=new ArrayList<ObjetoMapa>();

        while(c.moveToNext()){
        ObjetoMapa objetoMapa=new ObjetoMapa(
                        c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.ID)),
                        c.getInt(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.TIPO_ID)),
                        c.getDouble(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LATITUD)),
                        c.getDouble(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.LONGITUD)),
                        c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.USUARIO_ID)),
                        c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.NOMBRE_OBJETO)),
                        c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.DETALLES)),
                         c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.PAIS)),
                c.getString(c.getColumnIndex(DatuBaseKontratua.Objetos_mapa.CIUDAD))
        );
            ObjetoMapaArrayList.add(objetoMapa);

        }


        adapter= new ObjetosAdapter(ObjetoMapaArrayList);
        recyclerView = (RecyclerView)getView().findViewById(R.id.objetosRecyclerView);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return inflater.inflate(R.layout.fragment_fragment_list, container, false);
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
                    + " must implement OnFragmentInteractionListener");
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
