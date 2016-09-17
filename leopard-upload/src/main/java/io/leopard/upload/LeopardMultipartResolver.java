package io.leopard.upload;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class LeopardMultipartResolver extends CommonsMultipartResolver {

	private static final String[] NAMES = { "picture", "file", "image" };

	protected MultipartParsingResult parseFileItems(List<FileItem> fileItems, String encoding) {
		// System.out.println("fileItems:" + fileItems);
		MultipartParsingResult result = super.parseFileItems(fileItems, encoding);

		for (String fieldName : NAMES) {
			if (result.getMultipartFiles().get(fieldName) != null) {
				break;
			}
			FileItem item = this.find(fileItems, fieldName);
			if (item == null) {
				continue;
			}
			List<MultipartFile> list = this.toList(result, fieldName, item.getString());
			result.getMultipartFiles().put(fieldName, list);
			break;
		}
		// CommonsMultipartFile file = new CommonsMultipartFile(fileItem);
		// multipartFiles.add(file.getName(), file);
		return result;
	}

	protected FileItem find(List<FileItem> fileItems, String fieldName) {
		for (FileItem item : fileItems) {
			if (fieldName.equals(item.getFieldName())) {
				return item;
			}
		}
		return null;
	}

	protected List<MultipartFile> toList(MultipartParsingResult result, String name, String content) {
		List<MultipartFile> list = new ArrayList<MultipartFile>();
		MultipartFile file = new Base64MultipartFile(content);
		list.add(file);
		result.getMultipartFiles().put(name, list);
		return list;
	}

}
