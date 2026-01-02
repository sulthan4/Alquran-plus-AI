
import os
import xml.etree.ElementTree as ET
import glob

def validate_xml_files():
    files = glob.glob("androidApp/src/main/res/values-*/strings.xml")
    files.append("androidApp/src/main/res/values/strings.xml")
    
    has_error = False
    for file_path in files:
        try:
            ET.parse(file_path)
            # print(f"Valid: {file_path}")
        except ET.ParseError as e:
            print(f"INVALID XML: {file_path}")
            print(f"Error: {e}")
            has_error = True
        except Exception as e:
            print(f"Error processing {file_path}: {e}")
            has_error = True
            
    if not has_error:
        print("All XML files parsed successfully.")

if __name__ == "__main__":
    validate_xml_files()
