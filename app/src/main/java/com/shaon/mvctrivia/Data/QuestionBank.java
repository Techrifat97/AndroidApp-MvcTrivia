package com.shaon.mvctrivia.Data;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.shaon.mvctrivia.Controller.AppController;
import com.shaon.mvctrivia.Model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {
    ArrayList<Question > questionArrayList = new ArrayList<>();
    private final String URL = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    public List<Question> getQuestions(final QuestionlistAsyncRespone callBack){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++){
                            Question question = new Question();
                            try {
                                question.setQuestion(response.getJSONArray(i).get(0).toString());
                                question.setAnswer(response.getJSONArray(i).getBoolean(1));

                                questionArrayList.add(question);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if( null != callBack){
                            callBack.processFinished(questionArrayList);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
     return questionArrayList;
    }
}
