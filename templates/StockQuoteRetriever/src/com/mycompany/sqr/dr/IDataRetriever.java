package com.mycompany.sqr.dr;

import java.util.Calendar;
import java.util.List;

import com.mycompany.sqr.model.Quote;

/**
 * @author David Herzig
 */
public interface IDataRetriever {
    
    List<Quote> getData(String  symbol, Calendar from, Calendar to);
}
