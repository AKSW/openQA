function isDefined(obj) {
	return typeof obj !== "undefined";
}
function getLangValue(values, lang) {
	var value = null;
	values.forEach(function(entry) {
		if(entry.lang == lang) {
			value = entry.value;
			return false;
		}
	});
	return value;
}