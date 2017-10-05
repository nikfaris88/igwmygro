package com.ingeniworks.mykomunitimardi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingeniworks.mykomunitimardi.R;

import java.util.ArrayList;

public class FilesAdapter extends ArrayAdapter<String> {
    public FilesAdapter(Context context, ArrayList<String> data) {
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.files_row, parent, false);
        }
        // Lookup view for data population
        ImageView imgFileType = (ImageView) convertView.findViewById(R.id.imgFileType);
        TextView txtFileName = (TextView) convertView.findViewById(R.id.txtFileName);
        // Populate the data into the template view using the data object
        System.out.println("files: " + getItem(position));
        String fileType = getItem(position).substring(getItem(position).length() - 4);

        switch (fileType) {
            case ".pdf":
                imgFileType.setImageResource(R.drawable.ic_picture_as_pdf_black_24dp);
                break;
            case ".doc":
                imgFileType.setImageResource(R.drawable.ic_file_word);
                break;
            case "docx":
                imgFileType.setImageResource(R.drawable.ic_file_word);
                break;
            case ".ppt":
                imgFileType.setImageResource(R.drawable.ic_file_powerpoint);
                break;
            case "pptx":
                imgFileType.setImageResource(R.drawable.ic_file_powerpoint);
                break;
            case ".xls":
                imgFileType.setImageResource(R.drawable.ic_file_excel);
                break;
            case "xlsx":
                imgFileType.setImageResource(R.drawable.ic_file_excel);
                break;
            default:
                imgFileType.setImageResource(R.drawable.ic_error_black_24dp);
                break;
        }

        txtFileName.setText(getItem(position));
        // Return the completed view to render on screen
        return convertView;
    }
}