import sockets.HttpSocket;
import sockets.HttpSocketManager;


public class Main {

	public static void main(String[] args) {
		int initialPort = 9000;
		int apiClusterSize = 0; 
		int frontClusterSize = 0;
		int staticClusterSize = 0;
		
		System.out.println("Starting socket");
		for(int i=0; i<args.length; i++){
			if(args[i].indexOf("=")>0){
				String key = args[i].split("=")[0];
				String value = args[i].split("=")[1];
				if("initialPort".equalsIgnoreCase(key)){
					initialPort = Integer.valueOf(value);
				}
				if("apiClusterSize".equalsIgnoreCase(key)){
					apiClusterSize = Integer.valueOf(value);
					if(apiClusterSize>30){
						apiClusterSize = 30;
					}
				}
				if("frontClusterSize".equalsIgnoreCase(key)){
					frontClusterSize = Integer.valueOf(value);
					if(frontClusterSize>30){
						frontClusterSize = 30;
					}
				}
				if("staticClusterSize".equalsIgnoreCase(key)){
					staticClusterSize = Integer.valueOf(value);
					if(staticClusterSize>30){
						staticClusterSize = 30;
					}
				}
			}
		}
		
		apiClusterSize = 30; 
		frontClusterSize = 30;
		staticClusterSize = 30;
		
		
		//System.out.println("Listenning on port: " + initialPort + ". Please check it sending a request to http://localhost:" + serverPort + "/ping");
		int apiPort = initialPort;
		int frontEndPort = initialPort + 30;
		int staticPort = frontEndPort + 30;
		
		for(int i=0; i<apiClusterSize; i++){
			HttpSocket apiSocket = new HttpSocket(apiPort);
			HttpSocketManager.init();
			HttpSocketManager.executor.execute(apiSocket);
			System.out.println("Api server listenning on port: " + apiPort + ". Please check it sending a request to http://localhost:" + apiPort + "/ping");
			apiPort ++;
		}
		
		for(int i=0; i<frontClusterSize; i++){
			HttpSocket frontEndSocket = new HttpSocket(frontEndPort);
			HttpSocketManager.init();
			HttpSocketManager.executor.execute(frontEndSocket);
			System.out.println("Front End server listening on port: " + frontEndPort + ". Please check it sending a request to http://localhost:" + frontEndPort + "/ping");
			frontEndPort ++;
		}
		
		for(int i=0; i<staticClusterSize; i++){
			HttpSocket staticSocket = new HttpSocket(staticPort);
			HttpSocketManager.init();
			System.out.println("Front End server listening on port: " + staticPort + ". Please check it sending a request to http://localhost:" + staticPort + "/ping");
			HttpSocketManager.executor.execute(staticSocket);
			staticPort ++;
		}

	}

}
