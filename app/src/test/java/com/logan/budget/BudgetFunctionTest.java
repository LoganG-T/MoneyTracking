package com.logan.budget;

import com.logan.moneytracking.DateObject;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BudgetFunctionTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void budget_class_terms(){
        DateObject d = new DateObject();
        Budget budget = new Budget("Name", 500, d, d);
        budget.set_term(7);
        float terms = 71.42f;
        assertEquals(terms, budget.get_weekly_budget(), 0.001);
    }
    @Test
    public void budget_last_term(){
        DateObject d = new DateObject();
        Budget budget = new Budget("Name", 500, d, d);
        budget.set_term(7);
        float terms = 71.48f;
        assertEquals(terms, budget.get_last_week(), 0.001);
    }

    @Test
    public void budget_leftover(){
        DateObject d = new DateObject();
        Budget budget = new Budget("Name", 500, d, d);
        budget.set_term(7);
        budget.spend(50f);
        float terms = 64.28f;
        assertEquals(terms, budget.get_current_week(), 0.001);
    }

    @Test
    public void test_budget(){
        DateObject d = new DateObject("Monday", 30, 2020);
        DateObject d2 = new DateObject("Monday", 32, 2020);
        Budget budget = new Budget("budget", 500, d, d2);
        assertEquals(250f, budget.get_weekly_budget(),0.01f);
    }

    @Test
    public void test_current_budget(){
        DateObject d = new DateObject("Monday", 30, 2020);
        DateObject d2 = new DateObject("Monday", 32, 2020);
        Budget budget = new Budget("budget", 500, d, d2);
        assertEquals(250f, budget.get_current_week(),0.01f);
    }

    @Test
    public void test_week_gone(){
        DateObject d = new DateObject("Monday", 30, 2020);
        DateObject d2 = new DateObject("Monday", 32, 2020);
        Budget budget = new Budget("budget", 500, d, d2);
        budget.update_week();
        assertEquals(500f, budget.get_current_week(),0.01f);
    }

    @Test
    public void test_weeks_gone(){
        DateObject d = new DateObject("Monday", 30, 2020);
        DateObject d2 = new DateObject("Monday", 34, 2020);
        Budget budget = new Budget("budget", 100, d, d2);
        assertEquals(25f, budget.get_current_week(),0.01f);
        budget.update_week();
        budget.update_week();
        assertEquals(50f, budget.get_current_week(),0.01f);
    }

    @Test
    public void test_weeks_auto(){
        Calendar c = Calendar.getInstance();
        DateObject d = new DateObject("Monday", c.get(Calendar.WEEK_OF_YEAR) - 2, c.get(Calendar.YEAR));
        DateObject d2 = new DateObject("Monday", c.get(Calendar.WEEK_OF_YEAR) + 2, c.get(Calendar.YEAR));
        Budget budget = new Budget("budget", 100, d, d2);
        assertEquals(25f, budget.get_current_week(),0.01f);
        budget.update_week_auto();
        assertEquals(50f, budget.get_current_week(),0.01f);
    }

    @Test
    public void test_weeks_auto_after_end(){
        Calendar c = Calendar.getInstance();
        DateObject d = new DateObject("Monday", c.get(Calendar.WEEK_OF_YEAR) - 4, c.get(Calendar.YEAR));
        DateObject d2 = new DateObject("Monday", c.get(Calendar.WEEK_OF_YEAR) - 3, c.get(Calendar.YEAR));
        System.out.println(d2.toString());
        Budget budget = new Budget("budget", 100, d, d2);
        assertEquals(50f, budget.get_current_week(),0.01f);
        budget.update_week_auto();
        assertEquals(100f, budget.get_current_week(),0.01f);
    }

}