package com.gamesparks.client.core;

/**
 * Finite state machine for handshaking and communication with the GameSparks service.
 * 
 * @author nick redshaw
 * 
 */
public class ConnectorStateEngine {

    enum ConnectorState {
        STOPPED, CONNECTING_TO_LOADBALANCER, CONNECTING_TO_SERVICE, AUTHORISED, VALIDATION_FAILED
    }

    enum StateChangeEvent {
        NETWORK_AVAILABLE, SERVICE_URL_RECIEVED, SESSON_ID_RECEIVED, WS_DISCONNECT, VALIDATION_FAILURE, GENERAL_ERROR
    }

    private ConnectorState connectionState = ConnectorState.STOPPED;
    private ConnectorClient connectorClient;

    public ConnectorStateEngine(ConnectorClient connectorClient) {
        this.connectorClient = connectorClient;
    }

    /*
     * State machine for the connection state
     * 
     * @param event an event that may cause a state change
     */
    void changeState(StateChangeEvent event) {

        if (event == StateChangeEvent.GENERAL_ERROR) {
            enterStoppedState();
            return;
        }

        switch (connectionState) {

        case STOPPED:
            processStoppedEvents(event);
            break;

        case CONNECTING_TO_LOADBALANCER:
            processConnectingToLoadBalancerEvents(event);
            break;

        case CONNECTING_TO_SERVICE:
            processConnectingToServiceEvents(event);
            break;

        case AUTHORISED:
            processAuthorisedEvents(event);
            break;

        case VALIDATION_FAILED:
            // Game secret may be incorrect so there is no way out of this state
            break;

        }

    }

    void processAuthorisedEvents(StateChangeEvent event) {

        switch (event) {

        case WS_DISCONNECT:
            changeStateTo(ConnectorState.STOPPED);
            break;

        default:
            break;

        }
    }

    void processConnectingToServiceEvents(StateChangeEvent event) {

        switch (event) {

        case SESSON_ID_RECEIVED:
            changeStateTo(ConnectorState.AUTHORISED);
            connectorClient.processOfflineQueue();
            break;

        case VALIDATION_FAILURE:
            changeStateTo(ConnectorState.VALIDATION_FAILED);
            if (connectorClient.getWebSocket() != null) {
                connectorClient.getWebSocket().disconnect();
            }
            break;

        default:
            break;

        }
    }

    void processConnectingToLoadBalancerEvents(StateChangeEvent event) {

        switch (event) {

        case SESSON_ID_RECEIVED:
            changeStateTo(ConnectorState.AUTHORISED);
            break;

        case SERVICE_URL_RECIEVED:
            connectorClient.createWebSocket(connectorClient.getConnectUrl());
            changeStateTo(ConnectorState.CONNECTING_TO_SERVICE);
            break;

        default:
            break;

        }
    }

    public ConnectorState getConnectionState() {
        return connectionState;
    }

    void processStoppedEvents(StateChangeEvent event) {

        switch (event) {

        case NETWORK_AVAILABLE:
            connectorClient.createWebSocket(connectorClient.getServiceUrl());
            changeStateTo(ConnectorState.CONNECTING_TO_LOADBALANCER);
            break;

        default:
            break;

        }
    }

    void enterStoppedState() {
        changeStateTo(ConnectorState.STOPPED);
        if (connectorClient.getWebSocket() != null) {
            connectorClient.getWebSocket().disconnect();
        }
    }

    private void changeStateTo(ConnectorState state) {
        connectionState = state;
        connectorClient.getPlatformAbstractionLayer().logDebug("New state: " + connectionState);
    }

}
