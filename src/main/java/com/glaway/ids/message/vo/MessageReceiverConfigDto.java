package com.glaway.ids.message.vo;

import com.glaway.foundation.common.vdata.GLVData;

import javax.persistence.Basic;

/**
 * A representation of the model object '<em><b>MessageReceiverConfig</b></em>'.
 * <!-- begin-user-doc -->消息接收人配置<!-- end-user-doc -->
 * @author wangyangzan
 * @generated
 */

public class MessageReceiverConfigDto extends GLVData {

	/**
	 * <!-- begin-user-doc -->消息业务名称<!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String name = null;

	/**
	 * <!-- begin-user-doc -->接受者<!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String receiver = null;

	/**
	 * <!-- begin-user-doc -->接受者配置列表<!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Basic()
	private String receiverList = null;

	/**
	 * Returns the value of '<em><b>name</b></em>' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>name</b></em>' feature
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * @generated
	 */
	public void setName(String newName) {
		name = newName;
	}

	/**
	 * Returns the value of '<em><b>receiver</b></em>' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>receiver</b></em>' feature
	 * @generated
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @generated
	 */
	public void setReceiver(String newReceiver) {
		receiver = newReceiver;
	}

	/**
	 * Returns the value of '<em><b>receiverList</b></em>' feature.
	 *
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of '<em><b>receiverList</b></em>' feature
	 * @generated
	 */
	public String getReceiverList() {
		return receiverList;
	}

	/**
	 * @generated
	 */
	public void setReceiverList(String newReceiverList) {
		receiverList = newReceiverList;
	}

	/**
	 * A toString method which prints the values of all EAttributes of this
	 * instance. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		return "MessageReceiverConfig " + " [name: " + getName() + "]"
				+ " [receiver: " + getReceiver() + "]" + " [receiverList: "
				+ getReceiverList() + "]";
	}
}
