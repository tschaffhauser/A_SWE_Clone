package com.mycompany.sqr.dr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.mycompany.sqr.model.Quote;

/**
 * @author David Herzig
 */
public class YahooFinanceDataRetriever implements IDataRetriever {
    
    /** array index constants. */
    private static final int DATE_INDEX = 0;
    private static final int OPEN_INDEX = 1;
    private static final int HIGH_INDEX = 2;
    private static final int LOW_INDEX = 3;
    private static final int CLOSE_INDEX = 4;
    private static final int VOLUME_INDEX = 5;
    private static final int ADJCLOSE_INDEX = 6;
    
    /** Only existing instance. */
    private static YahooFinanceDataRetriever instance = null;

    private static final String URL = "http://ichart.finance.yahoo.com/table.csv?s=$symbol&a=$monthFrom&b=$dayFrom&c=$yearFrom&d=$monthTo&e=$dayTo&f=$yearTo&g=d&ignore=.csv";
    
    /**
     * Singleton Pattern method to retrieve the only existing instance.
     * @return the singleton object.
     */
    public static YahooFinanceDataRetriever getInstance() {
        if (instance == null) {
            instance = new YahooFinanceDataRetriever();
        }
        return instance;
    }

    /**
     * private ctr.
     */
    private YahooFinanceDataRetriever() {
    }
    
    @Override
    public List<Quote> getData(String symbol, Calendar from, Calendar to) {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
        
        String address = createDownloadURL(symbol, from, to);
        
        List<Quote> result = null;
        
        try {
            result = downloadData(address, symbol);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return result;
    }
    
    private String createDownloadURL(String symbol, Calendar from, Calendar to) {
        int dayFrom, dayTo, monthFrom, monthTo, yearFrom, yearTo;
        dayFrom = from.get(Calendar.DAY_OF_MONTH);
        dayTo = to.get(Calendar.DAY_OF_MONTH);
        monthFrom = from.get(Calendar.MONTH);
        monthTo = to.get(Calendar.MONTH);
        yearFrom = from.get(Calendar.YEAR);
        yearTo = to.get(Calendar.YEAR);
        
        // prepare url
        String address =
            URL.replace("$dayFrom", "" + dayFrom)
               .replace("$dayTo", "" + dayTo)
               .replace("$monthFrom", "" + monthFrom)
               .replace("$monthTo", "" + monthTo)
               .replace("$yearFrom", "" + yearFrom)
               .replace("$yearTo", "" + yearTo)
               .replace("$symbol", symbol);
        
        return address;
    }
    
    /**
     * Download the data from YAHOO.
     * @param address URL to download the data.
     * @param symbol The symbol of the requested quotes.
     * @return list of quote objects.
     * @throws Exception in case of an error.
     */
    private List<Quote> downloadData(String address, String symbol) throws Exception {
        List<Quote> result = new ArrayList<>();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(address);
        try (CloseableHttpResponse response = httpclient.execute(httpget)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                try (InputStream is = response.getEntity().getContent(); BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                    String line = br.readLine();
                    
                    // skip the first line with the headers
                    if (line != null) line = br.readLine();
                    
                    while (line != null) {
                        Quote quote = createObject(line, symbol);
                        result.add(quote);
                        line = br.readLine();
                    }
                }
            }
        }
        httpget.releaseConnection();
        httpclient.close();
        return result;
    }
    
    private Quote createObject(String line, String symbol) throws Exception {
        String [] tokens = line.split(",");
        Quote result = new Quote();
        result.setSymbol(symbol);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        result.setDate(sdf.parse(tokens[DATE_INDEX]));
        result.setAdjClose(Double.parseDouble(tokens[ADJCLOSE_INDEX]));
        result.setClose(Double.parseDouble(tokens[CLOSE_INDEX]));
        result.setHigh(Double.parseDouble(tokens[HIGH_INDEX]));
        result.setLow(Double.parseDouble(tokens[LOW_INDEX]));
        result.setOpen(Double.parseDouble(tokens[OPEN_INDEX]));
        result.setVolume(Long.parseLong(tokens[VOLUME_INDEX]));
        return result;
    }
    
}
