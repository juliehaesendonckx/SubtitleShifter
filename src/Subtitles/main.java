package Subtitles;

public class main {

	public static void main(String[] args) {
		SubtitleData subtitleData = new SubtitleData("C:\\Temp\\test.srt", false);
		subtitleData.readSubtitleFile();
		subtitleData.shiftSubtitles(ShiftAction.ADD, 5);
		subtitleData.exportSubtitles("C:\\Temp\\new_test.srt");
	}
}