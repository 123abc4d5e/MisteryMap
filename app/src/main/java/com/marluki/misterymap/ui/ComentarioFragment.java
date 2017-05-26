package com.marluki.misterymap.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.marluki.misterymap.R;
import com.marluki.misterymap.provider.DatuBaseKontratua;
import com.marluki.misterymap.utils.Cons;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class ComentarioFragment extends Fragment {

    private OnComentInsertListener mListener;
    private TextView txtComentario;
    private FloatingActionButton meterBtn;
    private String textoComentario, id, objeto_id;
    private String fecha;

    private ContentResolver resolver;

    public ComentarioFragment() {
        // Required empty public constructor
    }

    public static ComentarioFragment newInstance() {
        return new ComentarioFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comentario, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Cons.misPreferencias, Context.MODE_PRIVATE);
        id = sharedPreferences.getString(Cons.idCliente, null);
        objeto_id = getArguments().getString(DatuBaseKontratua.Comentarios.OBJETO_ID);

        meterBtn = (FloatingActionButton) view.findViewById(R.id.comentButton);
        txtComentario = (EditText) view.findViewById(R.id.comentarioTextoMeter);
//        if(TextUtils.isEmpty(txtComentario.getText())) {
//            meterBtn.hide();
//        }


        meterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textoComentario = txtComentario.getText().toString();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                fecha = simpleDateFormat.format(calendar.getTime());

                resolver = getActivity().getContentResolver();
                Uri uri = DatuBaseKontratua.Comentarios.URI_CONTENT;

                ContentValues contentValues = new ContentValues();
                contentValues.put(DatuBaseKontratua.Comentarios.ID, DatuBaseKontratua.Comentarios.generarIdComentario());
                contentValues.put(DatuBaseKontratua.Comentarios.USUARIO_ID, id);
                contentValues.put(DatuBaseKontratua.Comentarios.OBJETO_ID, objeto_id);
                contentValues.put(DatuBaseKontratua.Comentarios.TEXTO, textoComentario);
                contentValues.put(DatuBaseKontratua.Comentarios.COMENTARIO_ID, "null");
                contentValues.put(DatuBaseKontratua.Comentarios.FECHA, fecha);

                resolver.insert(uri, contentValues);

                mListener.onComentInsert();

            }
        });

//        txtComentario.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if(((TextView)v).getText().length()>5) {
//                    meterBtn.show();
//                }
//                return true;
//            }
//        });
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnComentInsertListener) {
            mListener = (OnComentInsertListener) context;
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


    public interface OnComentInsertListener {
        // TODO: Update argument type and name
        void onComentInsert();
    }
}
