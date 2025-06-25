module HandCoding {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.media;
	requires java.desktop;
	requires javafx.graphics;//�̰� ������ fxml �ȵ��ư�
	opens application to javafx.graphics, javafx.fxml;
}
