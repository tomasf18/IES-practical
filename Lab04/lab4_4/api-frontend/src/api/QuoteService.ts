import AxiosInstance from './AxiosInstance';

interface Quote {
    id: string;
    quote: string;
    movieId: string;
}

interface QuoteRequest {
    quote: string;
    movieId: string;
}

export const QuoteService = {
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
