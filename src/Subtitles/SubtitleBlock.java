package Subtitles;

import java.time.LocalTime;

/**
 * Represents a single subtitle block 
 * @author Julie
 *
 */
public class SubtitleBlock {
	private int _blockNumber;
	private String[] _text;
	private LocalTime _startTime;
	private LocalTime _endTime;
	
	public SubtitleBlock(int aBlockNumber, String aFirstLine, LocalTime aStartTime, LocalTime aEndTime) {
		super();
		this._blockNumber = aBlockNumber;		
		this._startTime = aStartTime;
		this._endTime = aEndTime;		
		String[] temp = { aFirstLine };
		this._text = temp;
	}
	
	public SubtitleBlock(int aBlockNumber, String aFirstLine, String aSecondLine, LocalTime aStartTime, LocalTime aEndTime) {
		super();
		this._blockNumber = aBlockNumber;		
		this._startTime = aStartTime;
		this._endTime = aEndTime;		
		String[] temp = { aFirstLine, aSecondLine };
		this._text = temp;
	}
	
	public String printBlock() {
		return _blockNumber + "\n" + _startTime.toString() + " --> " + _endTime.toString() + "\n" + printText();
	}
	
	private String printText() {
		switch (_text.length) {
		case 1:
			return _text[0] + "\n";
		case 2:
			return _text[0] + " " + _text[1] + "\n";
		default:
			return "";
		}
	}
	
	/**
	 * Add or subtract a given number of seconds
	 * @param aAction			Indicates if seconds are added or subtracted 
	 * @param aShiftInSeconds	A number of seconds that gets added or subtracted
	 */
	public void shiftSeconds(ShiftAction aAction, int aShiftInSeconds) {
		
		switch (aAction) {
			case SUBTRACT: 
				_startTime = _startTime.minusSeconds(aShiftInSeconds);
				_endTime = _endTime.minusSeconds(aShiftInSeconds);			
				break;
			default:
				_startTime = _startTime.plusSeconds(aShiftInSeconds);
				_endTime = _endTime.plusSeconds(aShiftInSeconds);
				break;
		}
	}
}