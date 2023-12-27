package data.twitterScraper;

import java.util.List;

public class TwitterModel {
    private String content;
    private String author;
    private String time;
    private List<String> hashtags;

    public TwitterModel(String content, String author, String time, List<String> hashtags) {
        this.setContent(content);
        this.setAuthor(author);
        this.setTime(time);
        this.setHashtags(hashtags);
    }

    // Getters (same as before)

    // Use Gson to serialize the object to JSON
    public String toJson() {
        com.google.gson.Gson gson = new com.google.gson.Gson();
        return gson.toJson(this);
    }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<String> getHashtags() {
		return hashtags;
	}

	public void setHashtags(List<String> hashtags) {
		this.hashtags = hashtags;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}