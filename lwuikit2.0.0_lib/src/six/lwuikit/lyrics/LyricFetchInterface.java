package six.lwuikit.lyrics;

public interface LyricFetchInterface {
	void onFetchStart(String identifier);
	void onFetchFail(String identifier);
	void onFetchSuccess(String identifier,String result);
}
