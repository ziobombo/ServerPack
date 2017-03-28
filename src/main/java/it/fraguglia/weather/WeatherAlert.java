package it.fraguglia.weather;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient;
import tk.plogitech.darksky.forecast.APIKey;
import tk.plogitech.darksky.forecast.ForecastException;
import tk.plogitech.darksky.forecast.ForecastRequest;
import tk.plogitech.darksky.forecast.ForecastRequestBuilder;
import tk.plogitech.darksky.forecast.GeoCoordinates;
import tk.plogitech.darksky.forecast.Latitude;
import tk.plogitech.darksky.forecast.Longitude;
import tk.plogitech.darksky.forecast.model.DataPoint;
import tk.plogitech.darksky.forecast.model.Forecast;

public class WeatherAlert {
	private static final String API_KEY = "api-key";	
	CommandLine line;
	
	public void configure(String[] args) {
		Options options = new Options();
		options.addOption(Option.builder(API_KEY).argName(API_KEY).desc("Dark sky api key").longOpt(API_KEY).hasArg().required().build());

		CommandLineParser parser = new DefaultParser();
		try {
			// parse the command line arguments
			line = parser.parse(options, args);
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		}
	}
	public static void main(String[] args) throws ForecastException {
		
		WeatherAlert wa = new WeatherAlert();
		//wa.configure(args);

		ForecastRequest request = new ForecastRequestBuilder()
		        .key(new APIKey("1a8567ebd72b836240966a021e646ebd"))
		        .time(Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS))
		        .language(ForecastRequestBuilder.Language.fr)
		        .units(ForecastRequestBuilder.Units.si)
		        .exclude(ForecastRequestBuilder.Block.alerts, ForecastRequestBuilder.Block.currently, ForecastRequestBuilder.Block.daily, ForecastRequestBuilder.Block.flags, ForecastRequestBuilder.Block.minutely)
		        .location(new GeoCoordinates(new Longitude(43.576309), new Latitude(7.112767))).build();

		DarkSkyJacksonClient client = new DarkSkyJacksonClient();
	    Forecast forecast = client.forecast(request);
	    XYSeriesCollection xyd = new XYSeriesCollection();
XYSeries s = new XYSeries("test");
for( DataPoint dp: forecast.getHourly().getData()){
	s.add(dp.getTime().get(ChronoField.HOUR_OF_DAY), dp.getWindSpeed());
}
xyd.addSeries(s);
	    JFreeChart jfc = ChartFactory.createXYLineChart("Vento per il giorno 01012017", "Ora", "Velocità del vento", (XYDataset)xyd, PlotOrientation.HORIZONTAL, true, true, true);
	    jfc.dr
	}
}
