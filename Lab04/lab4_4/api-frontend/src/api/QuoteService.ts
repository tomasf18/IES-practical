import AxiosInstance from './AxiosInstance';
import * as StompJs from "@stomp/stompjs";

interface Quote {
    id: string;
    quote: string;
    movieId: string;
}

interface QuoteRequest {
    quote: string;
    movieId: string;
}

let stompClient: StompJs.Client | null = null; 

export const QuoteService = {

    connectWebSocket: async (setQuotes: React.Dispatch<React.SetStateAction<Quote[]>>) => {

        if (stompClient && stompClient.active) {
            console.log("WebSocket already connected");
            return; 
        }

        stompClient = new StompJs.Client({
            brokerURL: "ws://localhost:8080/backend-ws",    
            debug: (str) => console.log(str),               
            reconnectDelay: 5000,                           
            heartbeatIncoming: 4000,                        
        });

        
        stompClient.onConnect = (frame) => {
            console.log("Connected: " + frame);

            stompClient?.subscribe("/topic/quotes", (message) => {
                const newQuote = JSON.parse(message.body);
                console.log("Received new quote: ", newQuote);

                setQuotes((prevQuotes) => {
                    const updatedQuotes = [...prevQuotes, newQuote];
                    return updatedQuotes.slice(-5);        
                });
            });
        };

        // Handle WebSocket errors
        stompClient.onWebSocketError = (error) => {
            console.error("WebSocket error: ", error);
        };

        // Handle STOMP protocol errors
        stompClient.onStompError = (frame) => {
            console.error("Broker reported error: " + frame.headers["message"]);
            console.error("Additional details: " + frame.body);
        };

        // Activate the client
        stompClient.activate();

        // Cleanup on component unmount
        return () => {
            if (stompClient && stompClient.active) {
                stompClient.deactivate();
                console.log("WebSocket connection closed");
            }
        };
    },

    createQuote: async (quoteRequest: QuoteRequest) => {
        try {
            const response = await AxiosInstance.post<Quote>('/quotes', quoteRequest);
            return response.data;
        } catch (error) {
            console.error('Error creating quote:', error);
            throw error;
        }
    },

    getQuoteById: async (id: string) => {
        try {
            const response = await AxiosInstance.get<Quote>(`/quotes/${id}`);
            return response.data;
        } catch (error) {
            console.error('Error fetching quote by ID:', error);
            throw error;
        }
    },

    getAllQuotes: async (movieId?: string) => {
        try {
            const params = movieId ? { movieId } : {};
            const response = await AxiosInstance.get<Quote[]>('/quotes', { params });
            return response.data;
        } catch (error) {
            console.error('Error fetching quotes:', error);
            throw error;
        }
    },

    getRandomQuote: async () => {
        try {
            const response = await AxiosInstance.get<Quote>('/quote');
            return response.data;
        } catch (error) {
            console.error('Error fetching random quote:', error);
            throw error;
        }
    },

    updateQuote: async (id: string, quoteRequest: QuoteRequest) => {
        try {
            const response = await AxiosInstance.put<Quote>(`/quotes/${id}`, quoteRequest);
            return response.data;
        } catch (error) {
            console.error('Error updating quote:', error);
            throw error;
        }
    },

    deleteQuote: async (id: string) => {
        try {
            await AxiosInstance.delete(`/quotes/${id}`);
            return { success: true };
        } catch (error) {
            console.error('Error deleting quote:', error);
            throw error;
        }
    },
};
