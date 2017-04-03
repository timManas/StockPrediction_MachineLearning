package GeneticAlgorithm;

/**
 * Created by timmanas on 2017-02-25.
 */
public class StockData {

    //region Constants
    private String CompanyName;
    private String SYMBOL;
    private String MarketCap;
    private double PE_Ratio;
    private double DivYield;
    private double Eps_Growth;
    private double Current_Ratio;
    private double ReturnOnInvestments;
    private double ReturnOnAssets;
    private double ReturnOnEquity;
    //endregion

    //region Getters

    public String getCompanyName() {
        return CompanyName;
    }

    public String getSYMBOL() {
        return SYMBOL;
    }

    public String getMarketCap() {
        return MarketCap;
    }

    public double getPE_Ratio() {
        return PE_Ratio;
    }

    public double getDivYield() {
        return DivYield;
    }

    public double getEps_Growth() {
        return Eps_Growth;
    }

    public double getCurrent_Ratio() {
        return Current_Ratio;
    }

    public double getReturnOnInvestments() {
        return ReturnOnInvestments;
    }

    public double getReturnOnAssets() {
        return ReturnOnAssets;
    }

    public double getReturnOnEquity() {
        return ReturnOnEquity;
    }

    //endregion


    //region Setters
    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public void setSYMBOL(String SYMBOL) {
        this.SYMBOL = SYMBOL;
    }

    public void setMarketCap(String marketCap) {
        MarketCap = marketCap;
    }

    public void setPE_Ratio(double PE_Ratio) {
        this.PE_Ratio = PE_Ratio;
    }

    public void setDivYield(double divYield) {
        DivYield = divYield;
    }

    public void setEps_Growth(double eps_Growth) {
        Eps_Growth = eps_Growth;
    }

    public void setCurrent_Ratio(double current_Ratio) {
        Current_Ratio = current_Ratio;
    }

    public void setReturnOnInvestments(double returnOnInvestments) {
        ReturnOnInvestments = returnOnInvestments;
    }

    public void setReturnOnAssets(double returnOnAssets) {
        ReturnOnAssets = returnOnAssets;
    }

    public void setReturnOnEquity(double returnOnEquity) {
        ReturnOnEquity = returnOnEquity;
    }
    //endregion



}
