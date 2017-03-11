package it.fraguglia.transmission;

import java.net.URI;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
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
	private CommandLine line;

	public TransmissionManager() {

	}

	public void configure(String[] args) {
		Options options = new Options();
		options.addOption("username", "username", true, "Transmission web username");
		options.addOption("password", "password", true, "Transmission web password");
		options.addOption("url", "url", true, "Transmission web url");

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
		rpcConfiguration.setHost(URI.create(line.getOptionValue("url")));
		rpcConfiguration.setUsername(line.getOptionValue("username"));
		rpcConfiguration.setPassword(line.getOptionValue("password"));

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
