package hr.fer.zemris.java.hw16.trazilica;

import java.nio.file.Path;

/**
 * Represents a document with a tf-idf vector and document file path.
 * 
 * @author Bruno Iljazović
 */
public class Document {
	
	/** The tf-idf vector. */
	Vector tfidf;
	
	/** The file path. */
	Path filePath;

	/**
	 * Instantiates a new document.
	 *
	 * @param tfidf the tfidf vector
	 * @param filePath the file path
	 */
	public Document(Vector tfidf, Path filePath) {
		this.tfidf = tfidf;
		this.filePath = filePath;
	}

	/**
	 * Gets the vector.
	 *
	 * @return the vector
	 */
	public Vector getTfidf() {
		return tfidf;
	}

	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	public Path getFilePath() {
		return filePath;
	}
}
