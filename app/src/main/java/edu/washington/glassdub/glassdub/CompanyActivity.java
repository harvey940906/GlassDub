package edu.washington.glassdub.glassdub;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kumulos.android.Kumulos;
import com.kumulos.android.ResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompanyActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private TextView title, descr;
    private LinearLayout rating;
    private Activity act = this;
    private ImageView companyImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        Intent intent = getIntent();
        String companyID = intent.getStringExtra("companyID");

        //companyImage = (ImageView) findViewById(R.id.companyImage);
        mViewPager = (ViewPager) findViewById(R.id.company_container);


        Bundle b = new Bundle();
        b.putString("companyID", companyID);

        Fragment reviewList = new ReviewList();
        reviewList.setArguments(b);

        Fragment interviewList = new InterviewList();
        interviewList.setArguments(b);

        Fragment jobList = new JobList();
        jobList.setArguments(b);

        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        vpAdapter.addFragments(reviewList, "Reviews");
        vpAdapter.addFragments(interviewList, "Interviews");
        vpAdapter.addFragments(jobList, "Jobs");

        mViewPager.setAdapter(vpAdapter);

        tabLayout = (TabLayout) findViewById(R.id.company_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        title = (TextView) findViewById(R.id.cTitle);
        descr = (TextView) findViewById(R.id.cDescr);
        rating = (LinearLayout) findViewById(R.id.cRating);

        Map<String,String> companyParams = new HashMap<>();
        companyParams.put("companyID", companyID);

        Kumulos.call("getCompany", companyParams, new ResponseHandler() {
            @Override
            public void didCompleteWithResult(Object result) {
                if (result.toString().equals("32") || result.toString().equals("64") || result.toString().equals("128")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            act);
                    alertDialogBuilder
                            .setTitle("Error")
                            .setMessage("We We were unable to retrieve information about the selected company. Try again.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // TODO: send them back to list of companies
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    ArrayList<LinkedHashMap<String, Object>> objects = (ArrayList<LinkedHashMap<String,Object>>) result;
                    if (objects.size() > 0) {
                        LinkedHashMap<String, Object> object = objects.get(0);
                        String companyName = object.get("name").toString();
                        title.setText(companyName);
//                        if(companyName.equals("Amazon")) {
//                            companyImage.setImageResource(R.drawable.amazon);
//                        } else if (companyName.equals("Google")) {
//                            companyImage.setImageResource(R.drawable.google);
//                        } else if (companyName.equals("Tableau")) {
//                            companyImage.setImageResource(R.drawable.tableau);
//                        } else if (companyName.equals("Zillow")) {
//                            companyImage.setImageResource(R.drawable.zillow);
//                        } else if (companyName.equals("Starbucks")) {
//                            companyImage.setImageResource(R.drawable.starbucks);
//                        } else {
//                            companyImage.setImageResource(R.drawable.facebook);
//                        }
                        descr.setText(object.get("description").toString());
                        // TODO: Show rating with stars
                        setRating(Integer.parseInt(object.get("rating").toString()));
                        // TODO: Do image stuff here
                        //String imgUrl = object.get("logo_url").toString();
                    }
                }
            }
        });
    }

    private void setRating(int count) {
        int[] stars = {R.id.star_1, R.id.star_2, R.id.star_3, R.id.star_4, R.id.star_5};
        for (int i = 0; i < count; i++) {
            ((ImageView) rating.findViewById(stars[i])).setImageResource(R.drawable.ic_star_gold_24dp);
        }
    }
}