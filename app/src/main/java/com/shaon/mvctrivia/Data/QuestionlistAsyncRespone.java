package com.shaon.mvctrivia.Data;

import com.shaon.mvctrivia.Model.Question;

import java.util.ArrayList;

public interface QuestionlistAsyncRespone {
    void processFinished(ArrayList<Question> questionArrayList);
}
