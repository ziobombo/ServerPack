package it.fraguglia.transmission;

import java.net.URI;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.stil4m.transmission.api.domain.RemoveTorrentInfo;
import nl.stil4m.transmission.api.domain.TorrentInfo;
import nl.stil4m.transmission.api.domain.TorrentInfoCollection;
import nl.stil4m.transmission.api.domain.ids.NumberListIds;
import nl.stil4m.transmission.rpc.RpcException;

public class TransmissionManager {
	private static final String URL = "url";
	private static final String PASSWORD = "password";
	private static final String USERNAME = "username";
	private CommandLine line;

	public TransmissionManager() {

	}

	public void configure(String[] args) {
		Options options = new Options();
		options.addOption(Option.builder(USERNAME).argName(USERNAME).desc("Transmission web username").longOpt(USERNAME).hasArg().required().build());
		options.addOption(Option.builder(PASSWORD).argName(PASSWORD).desc("Transmission web password").longOpt(PASSWORD).hasArg().required().build());
		options.addOption(
				Option.builder(URL).argName(URL).desc("Transmission web url").longOpt(URL).hasArg().required().build());

		CommandLineParser parser = new DefaultParser();
		try {
			// parse the command line arguments
			line = parser.parse(options, args);
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		}
	}

	public static void main(String[] args) throws RpcException {

		TransmissionManager tm = new TransmissionManager();
		tm.configure(args);
		tm.exec();
	}

	public void exec() throws RpcException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		final RpcConfiguration rpcConfiguration = new RpcConfiguration();
		rpcConfiguration.setHost(URI.create(line.getOptionValue(URL)));
		rpcConfiguration.setUsername(line.getOptionValue(USERNAME));
		rpcConfiguration.setPassword(line.getOptionValue(PASSWORD));

		RPCClient client = new RPCClient(rpcConfiguration, objectMapper);
		TransmissionRPCClient rpcClient = new TransmissionRPCClient(client);
		TorrentInfoCollection result = rpcClient.getAllTorrentsInfo();

		for (TorrentInfo ti : result.getTorrents()) {
			if (ti.getDoneDate() > 10000) {
				rpcClient.removeTorrent(new RemoveTorrentInfo(new NumberListIds(ti.getId()), false));
			}
		}

	}

}
