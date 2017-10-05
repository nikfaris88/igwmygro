package com.ingeniworks.mykomunitimardi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ingeniworks.mykomunitimardi.ComposeMessage;
import com.ingeniworks.mykomunitimardi.R;
import com.ingeniworks.mykomunitimardi.model.Project;
import com.ingeniworks.mykomunitimardi.model.Section;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ingeniworks.mykomunitimardi.SelectProject.isChat;

/**
 * Created by nikfaris on 20/09/2017.
 * SectionedAdapter for projects
 */

public class SectionedProjectAdapter extends SectioningAdapter {

    private static String LOG_TAG = "SectionedProjectAdapter";
    private Locale locale = Locale.getDefault();
    private static MyClickListener myClickListener;
    private ArrayList<Section> alSections = new ArrayList<>();
    private Context mContext;

    public SectionedProjectAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.rvlist_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.stick_header_item, parent, false);
        return new HeaderViewHolder(v);
    }

    @Override
    public int getNumberOfSections() {
        return alSections.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return alSections.get(sectionIndex).getProjects().size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return false;
    }

    private class ItemViewHolder extends SectioningAdapter.ItemViewHolder implements View.OnClickListener {
        TextView txtProjectTitle;
        TextView txtEOUser;
        TextView txtAgency;
        ImageView imgUser;
        ImageView imgChat;

        ItemViewHolder(View itemView) {
            super(itemView);
            txtProjectTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtEOUser = (TextView) itemView.findViewById(R.id.txtDescription1);
            txtAgency = (TextView) itemView.findViewById(R.id.txtDescription2);
            txtAgency.setVisibility(View.VISIBLE);
            imgUser = (ImageView) itemView.findViewById(R.id.imgUser);
            imgChat = (ImageView) itemView.findViewById(R.id.imgChat);
            if (!isChat) {
                imgChat.setVisibility(View.GONE);
                itemView.setOnClickListener(this);
            } else {

                imgChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgChat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Project project = getItem(getSection(), getPositionInSection());
                                Intent intent = new Intent(mContext, ComposeMessage.class);
                                System.out.println("projectID : " + project.getProject_id());
                                intent.putExtra("project_id", project.getProject_id());
                                String[] users = new String[project.getEnrollmentArrayList().size()];
                                for (int e = 0; e < project.getEnrollmentArrayList().size(); e++) {
                                    users[e] = project.getEnrollmentArrayList().get(e).getUser().getName();
                                }
                                intent.putExtra("enrollment", users);
                                mContext.startActivity(intent);
                            }
                        });
                    }
                });
            }


        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getSection(), getPositionInSection(), v);

        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        SectionedProjectAdapter.myClickListener = myClickListener;

    }


    private class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {
        TextView titleTextView;

        HeaderViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.txtHeader);
        }
    }


    public void setProjectBySection(List<Section> listSections) {
        alSections.clear();

        int section = 0;
        Section currentSection = null;

        for (int s = 0; s < listSections.size(); s++) {
            for (int p = 0; p < listSections.get(s).getProjects().size(); p++) {
                if (listSections.get(s).getProjects().get(p).getProject_categoryId() != section) {
                    if (currentSection != null) {
                        alSections.add(currentSection);
                    }
                    currentSection = new Section();
                    section = listSections.get(s).getProjects().get(p).getProject_categoryId();
                    currentSection.setSection_title(String.valueOf(section));
                }

                if (currentSection != null) {
                    currentSection.getProjects().add(listSections.get(s).getProjects().get(p));
                }
            }
        }
        alSections.add(currentSection);
        notifyAllSectionsDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, final int sectionIndex, final int itemIndex, int itemType) {


        ItemViewHolder ivh = (ItemViewHolder) viewHolder;

        Section s = alSections.get(sectionIndex);
        Project project = s.getProjects().get(itemIndex);
        try {
            String firstLetter = String.valueOf(project.getProject_title().charAt(0));

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            // generate random color
            int color = generator.getColor(project.getProject_title());
            //int color = generator.getRandomColor();

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter, color); // radius in px

            ivh.imgUser.setImageDrawable(drawable);
            ivh.txtProjectTitle.setText(project.getProject_title());
            ivh.txtEOUser.setText(getEnrollment(sectionIndex, itemIndex));
            ivh.txtAgency.setText(project.getAgency());

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        Section s = alSections.get(sectionIndex);
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;


        if (s.getSection_title().equalsIgnoreCase("1")) {
            hvh.titleTextView.setText("TANAMAN");
        } else if (s.getSection_title().equalsIgnoreCase("2")) {
            hvh.titleTextView.setText("TERNAKAN");
        } else if (s.getSection_title().equalsIgnoreCase("3")) {
            hvh.titleTextView.setText("PERIKANAN");
        }
    }

    private String capitalize(String s) {
        if (s != null && s.length() > 0) {
            return s.substring(0, 1).toUpperCase(locale) + s.substring(1);
        }

        return "";
    }

    private String pad(int spaces) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            b.append(' ');
        }
        return b.toString();
    }

    public interface MyClickListener {
        void onItemClick(int sectionIndex, int itemIndex, View v);
    }

    public Project getItem(int sectionIndex, int itemIndex) {
        Section s = alSections.get(sectionIndex);
        return s.getProjects().get(itemIndex);
    }

    private String getEnrollment(int sectionIndex, int itemIndex) {
        StringBuilder b = new StringBuilder();

        Section s = alSections.get(sectionIndex);
        Project p = s.getProjects().get(itemIndex);
        for (int e = 0; e < p.getEnrollmentArrayList().size(); e++) {
            b.append(p.getEnrollmentArrayList().get(e).getUser().getName()).append(", ");
        }

        return b.substring(0, b.length() - 1);
    }
}
