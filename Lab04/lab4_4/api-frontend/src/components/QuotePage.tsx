import { useEffect, useState } from 'react';
import { QuoteService } from '../api/QuoteService';

interface Quote {
  id: string;
  quote: string;
  movieId: string;
}

const QuotePage = () => {
  const [quotes, setQuotes] = useState<Quote[]>([]);
  const [loading, setLoading] = useState(true);
  const [quoteText, setQuoteText] = useState('');
  const [movieId, setMovieId] = useState('');
  const [searchMovieId, setSearchMovieId] = useState('');
  const [editMode, setEditMode] = useState(false);
  const [editQuoteId, setEditQuoteId] = useState<string | null>(null);

  useEffect(() => {
    fetchQuotes();
  }, []);

  const fetchQuotes = async () => {
    setLoading(true);
    try {
      const response = await QuoteService.getAllQuotes();
      setQuotes(response.data);
    } catch (error) {
      console.error("Error fetching quotes:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleAddQuote = async () => {
    const newQuote = { quote: quoteText, movieId };
    try {
      await QuoteService.createQuote(newQuote);
      alert('Quote created successfully');
      setQuoteText('');
      setMovieId('');
      fetchQuotes();
    } catch (error) {
      console.error("Error creating quote:", error);
      alert('Failed to create quote');
    }
  };

  const handleEditQuote = async () => {
    if (editQuoteId) {
      const updatedQuote = { id: editQuoteId, quote: quoteText, movieId };
      try {
        await QuoteService.updateQuote(editQuoteId, updatedQuote);
        alert('Quote updated successfully');
        setEditMode(false);
        setEditQuoteId(null);
        setQuoteText('');
        setMovieId('');
        fetchQuotes();
      } catch (error) {
        console.error("Error updating quote:", error);
        alert('Failed to update quote');
      }
    }
  };

  const handleDeleteQuote = async (id: string) => {
    try {
      await QuoteService.deleteQuote(id);
      alert('Quote deleted successfully');
      fetchQuotes();
    } catch (error) {
      console.error("Error deleting quote:", error);
      alert('Failed to delete quote');
    }
  };

  const handleFilterByMovieId = async () => {
    try {
      const response = await QuoteService.getAllQuotes(searchMovieId);
      setQuotes(response.data);
    } catch (error) {
      console.error("Error filtering quotes by movie ID:", error);
      alert('Failed to filter quotes');
    }
  };

  const handleGetRandomQuote = async () => {
    try {
      const response = await QuoteService.getRandomQuote();
      setQuotes([response.data]);
    } catch (error) {
      console.error("Error fetching random quote:", error);
      alert('Failed to fetch random quote');
    }
  };

  if (loading) return <p>Loading quotes...</p>;

  return (
    <div>
      <h1>Quotes</h1>

      {/* Add/Edit Quote */}
      <div>
        <h2>{editMode ? 'Edit Quote' : 'Add Quote'}</h2>
        <input
          placeholder="Quote Text"
          value={quoteText}
          onChange={(e) => setQuoteText(e.target.value)}
        />
        {!editMode && <input
          placeholder="Movie ID"
          value={movieId}
          onChange={(e) => setMovieId(e.target.value)}
        />}
        <button onClick={editMode ? handleEditQuote : handleAddQuote}>
          {editMode ? 'Update Quote' : 'Add Quote'}
        </button>
        {editMode && (
          <button onClick={() => { setEditMode(false); setEditQuoteId(null); setQuoteText(''); setMovieId(''); }}>
            Cancel Edit
          </button>
        )}
      </div>

      {/* Filter by Movie ID */}
      <div>
        <h2>Filter Quotes by Movie ID</h2>
        <input
          placeholder="Movie ID"
          value={searchMovieId}
          onChange={(e) => setSearchMovieId(e.target.value)}
        />
        <button onClick={handleFilterByMovieId}>Filter</button>
      </div>

      {/* Get Random Quote */}
      <div>
        <h2>Get Random Quote</h2>
        <button onClick={handleGetRandomQuote}>Random Quote</button>
      </div>

      {/* Quotes List */}
      <div>
        <h2>All Quotes</h2>
        <ul>
          {quotes.map((quote) => (
            <li key={quote.id}>
              "{quote.quote}" (Movie ID: {quote.movieId})
              <button onClick={() => {
                setEditMode(true);
                setEditQuoteId(quote.id);
                setQuoteText(quote.quote);
              }}>Edit</button>
              <button onClick={() => handleDeleteQuote(quote.id)}>Delete</button>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default QuotePage;
