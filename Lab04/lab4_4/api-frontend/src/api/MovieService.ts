import AxiosInstance from './AxiosInstance';
import * as StompJs from "@stomp/stompjs";

interface Movie {
    id: string;
    title: string;
    year: string;
}

interface MovieCreate {
    title: string;
    year: string;
}

let stompClient: StompJs.Client | null = null; // Keep track of the connection

export const MovieService = {
    
    connectWebSocket: async (setMovies: React.Dispatch<React.SetStateAction<Movie[]>>) => {

        if (stompClient && stompClient.active) {
            console.log("WebSocket already connected");
            return; // Prevent duplicate connection
        }

        stompClient = new StompJs.Client({
            brokerURL: "ws://localhost:8080/backend-ws",    // WebSocket URL
            debug: (str) => console.log(str),               // Optional debugging logs
            reconnectDelay: 5000,                           // Time to wait before attempting to reconnect
            heartbeatIncoming: 4000,                        // Heartbeat checks for incoming messages
        });

        // Define behavior on successful connection
        stompClient.onConnect = (frame) => {
            console.log("Connected: " + frame);

            // Subscribe to the topic and listen for updates
            stompClient?.subscribe("/topic/movies", (message) => {
                const newMovie = JSON.parse(message.body);
                console.log("Received new movie: ", newMovie);

                setMovies((prevMovies) => {
                    const updatedMovies = [...prevMovies, newMovie];
                    return updatedMovies.slice(-5);         // Keep only the last 5 movies
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

    createMovie: async (movie: MovieCreate) => {
        try {
            const response = await AxiosInstance.post<Movie>('/movies', movie);
            return response.data;
        } catch (error) {
            console.error('Error creating movie:', error);
            throw error;
        }
    },

    getMovieById: async (id: string) => {
        try {
            const response = await AxiosInstance.get<Movie>(`/movies/${id}`);
            return response.data;
        } catch (error) {
            console.error('Error fetching movie by ID:', error);
            throw error;
        }
    },

    getAllMovies: async (year?: string) => {
        try {
            const params = year ? { year } : {};
            const response = await AxiosInstance.get<Movie[]>('/movies', { params });
            return response.data;
        } catch (error) {
            console.error('Error fetching movies:', error);
            throw error;
        }
    },

    updateMovie: async (id: string, movie: Movie) => {
        try {
            const response = await AxiosInstance.put<Movie>(`/movies/${id}`, movie);
            return response.data;
        } catch (error) {
            console.error('Error updating movie:', error);
            throw error;
        }
    },

    deleteMovie: async (id: string) => {
        try {
            await AxiosInstance.delete(`/movies/${id}`);
            return { success: true };
        } catch (error) {
            console.error('Error deleting movie:', error);
            throw error;
        }
    },
};
