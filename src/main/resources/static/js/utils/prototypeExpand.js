/**
 * Created by Administrator on 2017/9/15.
 */
Date.prototype.format = function(fmt) {
  var o = {
    "M+": this.getMonth() + 1, //月份
    "d+": this.getDate(), //日
    "h+": this.getHours(), //小时
    "m+": this.getMinutes(), //分
    "s+": this.getSeconds(), //秒
    "q+": Math.floor((this.getMonth() + 3) / 3), //季度
    "S": this.getMilliseconds() //毫秒
  };
  if (/(y+)/.test(fmt))
    fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt))
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
  return fmt;
};

/*数组相关开始*/
Array.prototype.indexOf = function(val) {
  for (var i = 0; i < this.length; i++) {
    if (this[i] == val) return i;
  }
  return -1;
};
Array.prototype.indexOfByField = function(val, field) {
  for (var i = 0; i < this.length; i++) {
    if (this[i][field] == val[field]) return i;
  }
  return -1;
};
Array.prototype.removeByField = function(val, field) {
  var index = this.indexOfByField(val, field);
  if (index > -1) {
    this.splice(index, 1);
  }
};
Array.prototype.remove = function (val) {
  var index = this.indexOf(val);
  if (index > -1) {
    this.splice(index, 1);
  }
}; 
/*数组相关结束*/


/*自定义开始*/
/**
 *
 * 描述：js实现的map方法
 * @returns {Map}
 */
function Map() {
  var struct = function(key, value) {
    this.key = key;
    this.value = value;
  };
  // 添加map键值对
  var put = function(key, value) {
    for (var i = 0; i < this.arr.length; i++) {
      if (this.arr[i].key === key) {
        this.arr[i].value = value;
        return;
      }
    };
    this.arr[this.arr.length] = new struct(key, value);
  };
  //  根据key获取value
  var get = function(key) {
    for (var i = 0; i < this.arr.length; i++) {
      if (this.arr[i].key === key) {
        return this.arr[i].value;
      }
    }
    return null;
  };
  //   根据key删除
  var remove = function(key) {
    var v;
    for (var i = 0; i < this.arr.length; i++) {
      v = this.arr.pop();
      if (v.key === key) {
        continue;
      }
      this.arr.unshift(v);
    }
  };
  //   获取map键值对个数
  var size = function() {
    return this.arr.length;
  };
  // 判断map是否为空
  var isEmpty = function() {
    return this.arr.length <= 0;
  };
  // 清空
  var empty = function() {
    this.arr = new Array();
  };
  this.arr = new Array();
  this.get = get;
  this.put = put;
  this.remove = remove;
  this.size = size;
  this.isEmpty = isEmpty;
  this.empty = empty;
}
/*自定义结束*/