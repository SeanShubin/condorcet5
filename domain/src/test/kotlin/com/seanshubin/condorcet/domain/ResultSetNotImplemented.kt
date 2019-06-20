package com.seanshubin.condorcet.domain

import java.io.InputStream
import java.io.Reader
import java.math.BigDecimal
import java.net.URL
import java.sql.*
import java.sql.Array
import java.sql.Date
import java.util.*

abstract class ResultSetNotImplemented : ResultSet {
    override fun findColumn(columnLabel: String?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getNClob(columnIndex: Int): NClob {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getNClob(columnLabel: String?): NClob {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateNString(columnIndex: Int, nString: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateNString(columnLabel: String?, nString: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBinaryStream(columnIndex: Int, x: InputStream?, length: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBinaryStream(columnLabel: String?, x: InputStream?, length: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBinaryStream(columnIndex: Int, x: InputStream?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBinaryStream(columnLabel: String?, x: InputStream?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBinaryStream(columnIndex: Int, x: InputStream?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBinaryStream(columnLabel: String?, x: InputStream?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getStatement(): Statement {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateTimestamp(columnIndex: Int, x: Timestamp?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateTimestamp(columnLabel: String?, x: Timestamp?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateNCharacterStream(columnIndex: Int, x: Reader?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateNCharacterStream(columnLabel: String?, reader: Reader?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateNCharacterStream(columnIndex: Int, x: Reader?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateNCharacterStream(columnLabel: String?, reader: Reader?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateInt(columnIndex: Int, x: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateInt(columnLabel: String?, x: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun moveToInsertRow() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getDate(columnIndex: Int): Date {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getDate(columnLabel: String?): Date {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getDate(columnIndex: Int, cal: Calendar?): Date {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getDate(columnLabel: String?, cal: Calendar?): Date {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getWarnings(): SQLWarning {
        throw UnsupportedOperationException("not implemented")
    }

    override fun beforeFirst() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun close() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateFloat(columnIndex: Int, x: Float) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateFloat(columnLabel: String?, x: Float) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getBoolean(columnIndex: Int): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getBoolean(columnLabel: String?): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isFirst(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getBigDecimal(columnIndex: Int, scale: Int): BigDecimal {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getBigDecimal(columnLabel: String?, scale: Int): BigDecimal {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getBigDecimal(columnIndex: Int): BigDecimal {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getBigDecimal(columnLabel: String?): BigDecimal {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBytes(columnIndex: Int, x: ByteArray?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBytes(columnLabel: String?, x: ByteArray?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isLast(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun insertRow() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getTime(columnIndex: Int): Time {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getTime(columnLabel: String?): Time {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getTime(columnIndex: Int, cal: Calendar?): Time {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getTime(columnLabel: String?, cal: Calendar?): Time {
        throw UnsupportedOperationException("not implemented")
    }

    override fun rowDeleted(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun last(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isAfterLast(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun relative(rows: Int): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun absolute(row: Int): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getSQLXML(columnIndex: Int): SQLXML {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getSQLXML(columnLabel: String?): SQLXML {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T : Any?> unwrap(iface: Class<T>?): T {
        throw UnsupportedOperationException("not implemented")
    }

    override fun next(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getFloat(columnIndex: Int): Float {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getFloat(columnLabel: String?): Float {
        throw UnsupportedOperationException("not implemented")
    }

    override fun wasNull(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getRow(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun first(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateAsciiStream(columnIndex: Int, x: InputStream?, length: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateAsciiStream(columnLabel: String?, x: InputStream?, length: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateAsciiStream(columnIndex: Int, x: InputStream?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateAsciiStream(columnLabel: String?, x: InputStream?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateAsciiStream(columnIndex: Int, x: InputStream?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateAsciiStream(columnLabel: String?, x: InputStream?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getURL(columnIndex: Int): URL {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getURL(columnLabel: String?): URL {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateShort(columnIndex: Int, x: Short) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateShort(columnLabel: String?, x: Short) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getType(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateNClob(columnIndex: Int, nClob: NClob?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateNClob(columnLabel: String?, nClob: NClob?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateNClob(columnIndex: Int, reader: Reader?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateNClob(columnLabel: String?, reader: Reader?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateNClob(columnIndex: Int, reader: Reader?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateNClob(columnLabel: String?, reader: Reader?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateRef(columnIndex: Int, x: Ref?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateRef(columnLabel: String?, x: Ref?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateObject(columnIndex: Int, x: Any?, scaleOrLength: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateObject(columnIndex: Int, x: Any?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateObject(columnLabel: String?, x: Any?, scaleOrLength: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateObject(columnLabel: String?, x: Any?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setFetchSize(rows: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun afterLast() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateLong(columnIndex: Int, x: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateLong(columnLabel: String?, x: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getBlob(columnIndex: Int): Blob {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getBlob(columnLabel: String?): Blob {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateClob(columnIndex: Int, x: Clob?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateClob(columnLabel: String?, x: Clob?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateClob(columnIndex: Int, reader: Reader?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateClob(columnLabel: String?, reader: Reader?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateClob(columnIndex: Int, reader: Reader?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateClob(columnLabel: String?, reader: Reader?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getByte(columnIndex: Int): Byte {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getByte(columnLabel: String?): Byte {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getString(columnIndex: Int): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getString(columnLabel: String?): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateSQLXML(columnIndex: Int, xmlObject: SQLXML?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateSQLXML(columnLabel: String?, xmlObject: SQLXML?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateDate(columnIndex: Int, x: Date?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateDate(columnLabel: String?, x: Date?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getHoldability(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getObject(columnIndex: Int): Any {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getObject(columnLabel: String?): Any {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getObject(columnIndex: Int, map: MutableMap<String, Class<*>>?): Any {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getObject(columnLabel: String?, map: MutableMap<String, Class<*>>?): Any {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T : Any?> getObject(columnIndex: Int, type: Class<T>?): T {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T : Any?> getObject(columnLabel: String?, type: Class<T>?): T {
        throw UnsupportedOperationException("not implemented")
    }

    override fun previous(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateDouble(columnIndex: Int, x: Double) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateDouble(columnLabel: String?, x: Double) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getLong(columnIndex: Int): Long {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getLong(columnLabel: String?): Long {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getClob(columnIndex: Int): Clob {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getClob(columnLabel: String?): Clob {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBlob(columnIndex: Int, x: Blob?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBlob(columnLabel: String?, x: Blob?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBlob(columnIndex: Int, inputStream: InputStream?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBlob(columnLabel: String?, inputStream: InputStream?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBlob(columnIndex: Int, inputStream: InputStream?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBlob(columnLabel: String?, inputStream: InputStream?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateByte(columnIndex: Int, x: Byte) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateByte(columnLabel: String?, x: Byte) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateRow() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun deleteRow() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isClosed(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getNString(columnIndex: Int): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getNString(columnLabel: String?): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getCursorName(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getArray(columnIndex: Int): Array {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getArray(columnLabel: String?): Array {
        throw UnsupportedOperationException("not implemented")
    }

    override fun cancelRowUpdates() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateString(columnIndex: Int, x: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateString(columnLabel: String?, x: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setFetchDirection(direction: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getFetchSize(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getCharacterStream(columnIndex: Int): Reader {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getCharacterStream(columnLabel: String?): Reader {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isBeforeFirst(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBoolean(columnIndex: Int, x: Boolean) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBoolean(columnLabel: String?, x: Boolean) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun refreshRow() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun rowUpdated(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBigDecimal(columnIndex: Int, x: BigDecimal?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateBigDecimal(columnLabel: String?, x: BigDecimal?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getShort(columnIndex: Int): Short {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getShort(columnLabel: String?): Short {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getAsciiStream(columnIndex: Int): InputStream {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getAsciiStream(columnLabel: String?): InputStream {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateTime(columnIndex: Int, x: Time?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateTime(columnLabel: String?, x: Time?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getTimestamp(columnIndex: Int): Timestamp {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getTimestamp(columnLabel: String?): Timestamp {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getTimestamp(columnIndex: Int, cal: Calendar?): Timestamp {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getTimestamp(columnLabel: String?, cal: Calendar?): Timestamp {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getRef(columnIndex: Int): Ref {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getRef(columnLabel: String?): Ref {
        throw UnsupportedOperationException("not implemented")
    }

    override fun moveToCurrentRow() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getConcurrency(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateRowId(columnIndex: Int, x: RowId?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateRowId(columnLabel: String?, x: RowId?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getNCharacterStream(columnIndex: Int): Reader {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getNCharacterStream(columnLabel: String?): Reader {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateArray(columnIndex: Int, x: Array?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateArray(columnLabel: String?, x: Array?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getBytes(columnIndex: Int): ByteArray {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getBytes(columnLabel: String?): ByteArray {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getDouble(columnIndex: Int): Double {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getDouble(columnLabel: String?): Double {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getUnicodeStream(columnIndex: Int): InputStream {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getUnicodeStream(columnLabel: String?): InputStream {
        throw UnsupportedOperationException("not implemented")
    }

    override fun rowInserted(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isWrapperFor(iface: Class<*>?): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getInt(columnIndex: Int): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getInt(columnLabel: String?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateNull(columnIndex: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateNull(columnLabel: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getRowId(columnIndex: Int): RowId {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getRowId(columnLabel: String?): RowId {
        throw UnsupportedOperationException("not implemented")
    }

    override fun clearWarnings() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getMetaData(): ResultSetMetaData {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getBinaryStream(columnIndex: Int): InputStream {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getBinaryStream(columnLabel: String?): InputStream {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateCharacterStream(columnIndex: Int, x: Reader?, length: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateCharacterStream(columnLabel: String?, reader: Reader?, length: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateCharacterStream(columnIndex: Int, x: Reader?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateCharacterStream(columnLabel: String?, reader: Reader?, length: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateCharacterStream(columnIndex: Int, x: Reader?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun updateCharacterStream(columnLabel: String?, reader: Reader?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getFetchDirection(): Int {
        throw UnsupportedOperationException("not implemented")
    }
}