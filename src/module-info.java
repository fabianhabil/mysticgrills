/**
 * 
 */
/**
 * 
 */
module mysticgrills {
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.base;
	requires java.sql;

	opens mysticgrills;
	opens mysticgrills.model;
	opens mysticgrills.view.home;
	opens mysticgrills.view.user;
	opens mysticgrills.controller;
//	opens mysticgrills.view;
}