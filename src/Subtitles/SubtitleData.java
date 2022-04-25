package Subtitles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the content of a subtitle file
 * @author Julie
 *
 */
public class SubtitleData {
	private String _filename;
	private int _numberOfBlocks;
	private ArrayList<SubtitleBlock> _blocks;
	private boolean _logging;
	

	public SubtitleData(boolean aLoggingOn) {
		this._filename = "";
		this._numberOfBlocks = 0;
		this._blocks = new ArrayList<SubtitleBlock>();	
		this._logging = aLoggingOn;	
	}
	
	public SubtitleData(String aFilename, boolean aLoggingOn) {
		this._filename = aFilename;
		this._numberOfBlocks = 0;
		this._blocks = new ArrayList<SubtitleBlock>();
		this._logging = aLoggingOn;
	}	
	
	private void updateNrOfBlocks() {
		_numberOfBlocks = _blocks.size();
	}
	
	public int nrOfBlocks() {
		return _numberOfBlocks;
	}
	
	/**
	 * Adds a subtitle block to the data list
	 * @param aBlock	The SubtitleBlock that gets added to the subtitle collection.
	 */
	public void addData(SubtitleBlock aBlock) {
		_blocks.add(aBlock);	
		updateNrOfBlocks();
	}
	
	/**
	 * Read the content of the subtitle file and convert the content into subtitle blocks
	 */
	public void readSubtitleFile() {
		BufferedReader objReader = null;
		try {
			objReader = new BufferedReader(new FileReader(_filename));
			String line = "";
			List<String> content = new ArrayList<String>();
			SubtitleData data = new SubtitleData(false);
			
			while( (line = objReader.readLine()) != null) {
				if (_logging) 
					System.out.println("DEBUG: 'readSubtitleFile' >> read line " + line);
				
				content.add(line);		
			}
			      
			_blocks = data.generateBlocks(content);
			updateNrOfBlocks();
			
			if (_logging) 
				System.out.println("DEBUG: 'readSubtitleFile' >> Nr of blocks is " + _numberOfBlocks);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
			
	/**
	 * Convert the content of the subtitle file into subtitle blocks.
	 * @param aFileContent		The content of the subtitle file, every entry in the list is a line from the file.
	 * @return An ArrayList of SubtitleBlocks taken from the provided file.
	 */
	public ArrayList<SubtitleBlock> generateBlocks(List<String> aFileContent) {
		int blockIterator = 0;		
		ArrayList<SubtitleBlock> tempList = new ArrayList<SubtitleBlock>();
		
		for (int i = 0; i < aFileContent.size(); i++) {
			try {				
				if (!aFileContent.get(i).equals("")) {
					String blockCount = aFileContent.get(i);
					blockCount = blockCount.replaceAll("[^0-9]", "");					
					blockIterator = Integer.parseInt(blockCount);
					
					String file_timestamp = aFileContent.get(i + 1);
					String file_text1 = aFileContent.get(i + 2);
					String file_text2 = "";
					
					LocalTime start = LocalTime.of(0,0,0);
					LocalTime end = LocalTime.of(0,0,0);
					String[] times = splitTimeStamp(file_timestamp);					
					if (times != null) {
						start = LocalTime.parse(times[0].strip());
				 		end = LocalTime.parse(times[1].strip());						
					}	
					
					if (i + 3 < aFileContent.size())
						file_text2 = aFileContent.get(i + 3);
					
					SubtitleBlock stBlock; 
					if (file_text2.equals("")) {
						stBlock = new SubtitleBlock(blockIterator, file_text1, start, end);
						i = i + 3;
					} else {
						stBlock = new SubtitleBlock(blockIterator, file_text1, file_text2, start, end);
						i = i + 4;
					}
									
					tempList.add(stBlock);
					
					if (_logging) 
						System.out.println("DEBUG: 'generateBlocks' >> New block is " + stBlock.printBlock());
				}				
			} catch (Exception e) {
				System.err.println(e.getMessage());
				return null;		
			}
		}		

		if (_logging) 
			System.out.println("DEBUG: 'generateBlocks' >> Size tempList " + tempList.size());
		
		return tempList;
	}
	
	/**
	 * Write the collected subtitle data to a new file.
	 * @param aPath		The path of the new file that is created.
	 */
	public void exportSubtitles(String aPath) {
		try {
            FileWriter fWriter = new FileWriter(aPath);
            for (SubtitleBlock subtitleBlock : _blocks) 
				fWriter.write(subtitleBlock.printBlock() + "\n");
				
            fWriter.flush();
            fWriter.close();
            
    		if (_logging) 
    			System.out.println("DEBUG: 'exportSubtitles' >> The new subtitle file is created successfully, path is " + aPath);
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
	}
		
	/**
	 * Split the time stamp string into start & end time
	 * @param aTimeSerie	A string that contains the start & end time of the subtitle block
	 * @return	An array of two values (start & end time) || null if the input is invalid 
	 */
	private String[] splitTimeStamp(String aTimeSerie) {
		String[] result;		
		String timeSerie = aTimeSerie.replace(",", ".");
		result = timeSerie.split("-->");
		
		if (result.length == 2) 		
			return result;
			
		return null;
	}
	
	/**
	 * Shift the time stamp of a subtitle block based on the given arguments
	 * @param aAction			Indicates if seconds are added or subtracted 
	 * @param aShiftInSeconds	A number of seconds that gets added or subtracted
	 */
	public void shiftSubtitles(ShiftAction aAction, int aShiftInSeconds) {
		for (SubtitleBlock subtitleBlock : _blocks) {
			subtitleBlock.shiftSeconds(aAction, aShiftInSeconds);
		}
	}
}