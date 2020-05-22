/**
 * 封装WebUploader
 */

// 判断浏览器是否支持图片的base64
function isSupportBase64() {
    var data = new Image();
    var support = true;
    data.onload = data.onerror = function() {
        if( this.width != 1 || this.height != 1 ) {
            support = false;
        }
    }
    data.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
    return support;
}

// 检测是否已经安装flash，检测flash的版本
function flashVersion() {
    var version;

    try {
        version = navigator.plugins[ 'Shockwave Flash' ];
        version = version.description;
    } catch ( ex ) {
        try {
            version = new ActiveXObject('ShockwaveFlash.ShockwaveFlash')
                .GetVariable('$version');
        } catch ( ex2 ) {
            version = '0.0';
        }
    }
    version = version.match( /\d+/g );
    return parseFloat( version[ 0 ] + '.' + version[ 1 ], 10 );
}

function supportTransition(){
    var s = document.createElement('p').style,
        r = 'transition' in s ||
            'WebkitTransition' in s ||
            'MozTransition' in s ||
            'msTransition' in s ||
            'OTransition' in s;
    s = null;
    return r;
}
//判断是否支持
function isSupport($uploadMainContainer){
    if ( !WebUploader.Uploader.support('flash') && WebUploader.browser.ie ) {

        // flash 安装了但是版本过低。
        if (flashVersion()) {
            (function(container) {
                window['expressinstallcallback'] = function( state ) {
                    switch(state) {
                        case 'Download.Cancelled':
                            alert('您取消了更新！')
                            break;

                        case 'Download.Failed':
                            alert('安装失败')
                            break;

                        default:
                            alert('安装已成功，请刷新！');
                            break;
                    }
                    delete window['expressinstallcallback'];
                };

                var swf = 'expressInstall.swf';
                // insert flash object
                var html = '<object type="application/' +
                    'x-shockwave-flash" data="' +  swf + '" ';

                if (WebUploader.browser.ie) {
                    html += 'classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" ';
                }

                html += 'width="100%" height="100%" style="outline:0">'  +
                    '<param name="movie" value="' + swf + '" />' +
                    '<param name="wmode" value="transparent" />' +
                    '<param name="allowscriptaccess" value="always" />' +
                    '</object>';

                container.html(html);

            })($uploadMainContainer);

            // 压根就没有安转。
        } else {
            $uploadMainContainer.html('<a href="http://www.adobe.com/go/getflashplayer" target="_blank" border="0"><img alt="get flash player" src="http://www.adobe.com/macromedia/style_guide/images/160x41_Get_Flash_Player.jpg" /></a>');
        }
        return;
    } else if (!WebUploader.Uploader.support()) {
        alert( 'Web Uploader 不支持您的浏览器！');
        return;
    }
}


function SkWebUploader(opt) {
    isSupport();
    var $this = this;
    // 所有文件的进度信息，key为file id
    this.percentages = {};
    // 优化retina, 在retina下这个值是2
    var ratio = window.devicePixelRatio || 1;
    var skExpand = opt.skExpand;
    this.skExpand = skExpand;
    //预览宽高
    this.thumbnailWidth = skExpand.thumbnailWidth || 110 * ratio;
    this.thumbnailHeight = skExpand.thumbnailHeight || 110 * ratio;
    // 添加的文件数量
    this.fileCount = 0;
    // 初始的文件数量
    this.initFileCount = 0;
    var labels = ['图片','视频','文件'];
    var labelUnits = ['张','个','个'];
    this.labelType = opt.labelType||0;
    this.label = labels[this.labelType];
    this.labelUnit = labelUnits[this.labelType];
    // 添加的文件总大小
    this.fileSize = 0;
    this.$uploadMainContainer = $(skExpand.containerId);
    this.state = skExpand.state||'pedding';
    // 状态栏，包括进度和控制按钮
    this.$statusBar = this.$uploadMainContainer.find('.statusBar');
    // 上传按钮
    this.$upload = this.$uploadMainContainer.find('.uploadBtn');
    // 没选择文件之前的内容。
    this.$placeHolder = this.$uploadMainContainer.find('.placeholder');
    this.$progress = this.$statusBar.find('.progress').hide();

    // 图片容器
    this.$queue = $('<ul class="filelist"></ul>')
        .appendTo(this.$uploadMainContainer.find( '.queueList' ));
    // 文件总体选择信息。
    this.$info = this.$statusBar.find('.info');
    this.$continueAdd = this.$uploadMainContainer.find('.continueAdd');
    //已经上传的文件名
    this.fileNames = [];
    this.uploader = WebUploader.create({
        pick: opt.pick||{
            id: skExpand.containerId+' .filePicker',
            label: '点击选择'+$this.label
        },
        formData: opt.formData||{},
        fileVal:opt.fileVal||'file',//上传字段名
        dnd: opt.dnd,
        paste: '#uploader',
        swf: opt.swf||basePath+'static/H-ui.admin/lib/webuploader/0.1.5/Uploader.swf',
        chunked: opt.chunked,
        chunkSize: 512 * 1024,
        server: opt.server||basePath+'upload/file',
        threads:opt.threads||10,
        // runtimeOrder: 'flash',

        accept: opt.accept,
        // accept: {
        //     title: 'Images',
        //     extensions: 'gif,jpg,jpeg,bmp,png',
        //     mimeTypes: 'image/*'
        // },

        // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
        disableGlobalDnd: opt.disableGlobalDnd,
        fileNumLimit: opt.fileNumLimit||10,
        fileSizeLimit: opt.fileSizeLimit||200 * 1024 * 1024,    // 200 M
        fileSingleSizeLimit: opt.fileSingleSizeLimit||50 * 1024 * 1024    // 50 M
    });
    initMethod(this);
    // 添加“添加文件”的按钮，
    this.uploader.addButton({
        id: skExpand.containerId+' .continueAdd',
        label: '继续添加'
    });
    // 拖拽时不接受 js, txt 文件。
    this.uploader.on( 'dndAccept', dndAccept);
    this.uploader.on('dialogOpen', function() {
        console.log('here');
    });
    this.uploader.onError = function( code ) {
        if('Q_EXCEED_NUM_LIMIT' == code){
            sk.failMsg( '最多上传文件数为: ' + this.option('fileNumLimit'));
        }else{
            alert( 'Eroor: ' + code );
        }
    };
    this.uploader.on( 'all', function( type ) {
        var stats;
        switch( type ) {
            case 'uploadFinished':
                $this.setState( 'confirm' );
                break;
            case 'startUpload':
                $this.setState( 'uploading' );
                break;
            case 'stopUpload':
                $this.setState( 'paused' );
                break;

        }
    });
    this.uploader.onFileDequeued = function( file ) {
        $this.fileCount--;
        $this.fileSize -= file.size;

        if ( !$this.fileCount ) {
            $this.setState( 'pedding' );
        }

        $this.removeFile(file);
        $this.updateTotalProgress();

    };
    this.uploader.onUploadProgress = function( file, percentage ) {
        var $li = $('#'+file.id),
            $percent = $li.find('.progress span');

        $percent.css( 'width', percentage * 100 + '%' );
        $this.percentages[ file.id ][ 1 ] = percentage;
        $this.updateTotalProgress();
    };
    this.uploader.onFileQueued = function( file ) {
        $this.fileCount++;
        $this.fileSize += file.size;

        if ( $this.fileCount === 1 ) {
            $this.$placeHolder.addClass( 'element-invisible' );
            $this.$statusBar.show();
        }
        // console.log(file);
        $this.addFile(file);
        if ('state-complete' == file.clazz){//修改界面用到
            $this.setState( 'ready' );
            $this.setState( 'complete' );
            $('#'+file.id).append( '<span class="success"></span>' );
        }else{
            $this.setState( 'ready' );
        }

        $this.updateTotalProgress();
    };
    //当文件上传成功时触发。
    this.uploader.on('uploadSuccess',function( file,r) {
        $('#'+file.id+'>p.title').attr('name',r.data.fileNames.join(','));
        if (skExpand&&typeof skExpand.uploadSuccess === 'function'){
            skExpand.uploadSuccess(file,r);
        }else{
            if (r.code == '0'){
                // sk.successMsg(r.msg);
                console.log(r.msg);
                $.merge($this.fileNames,r.data.fileNames);
                // console.log(r.data)
                // console.log(fileNames)
            }else{
                sk.failMsg(r.msg);
                console.error(r.msg);
            }
        }
    });
    //当所有文件上传结束时触发
    this.uploader.on('uploadFinished',function() {
        if (skExpand&&skExpand.fieldId){
            console.log($this.fileNames.join(';'));
            $(skExpand.fieldId).val($this.fileNames.join(';'));
        }
        if (skExpand&&typeof skExpand.uploadFinished === 'function'){
            skExpand.uploadFinished();
        }
        if (typeof $this.onUploadFinished === 'function'){
            $this.onUploadFinished()
        }
        console.log('文件上传结束')
    });

    if (this.$info){
        this.$info.on( 'click', '.retry', function() {
            $this.uploader.retry();
        } );
        this.$info.on( 'click', '.ignore', function() {
            alert( 'todo' );
        } );
    }

    this.uploadClick = function (){
        if ( $($this.$upload).hasClass( 'disabled' ) ) {
            return false;
        }
        // if ( $(this).hasClass( 'disabled' ) ) {
        //     return false;
        // }
        var state = $this.state;
        if ( state === 'ready' ) {
            $this.uploader.upload();
        } else if ( state === 'paused' ) {
            $this.uploader.upload();
        } else if ( state === 'uploading' ) {
            $this.uploader.stop();
        }
    }
    this.$upload.on('click', this.uploadClick);
    this.$upload.addClass( 'state-' + this.state );
    this.updateTotalProgress = updateTotalProgress;
    this.updateTotalProgress();

    //当前拖动的对象
    this.dragEl = null;
    init(this)
}
function initMethod($this) {

    $this.addFile = addFile;
    $this.removeFile = removeFile;
    $this.updateStatus = updateStatus;
    $this.updateTotalProgress = updateTotalProgress;
    $this.setState = setState;
    $this.initList = initList;
    //判断是否已经全部上传
    $this.isUpload = function () {
        if (this.state === 'finish' || (this.uploader.getFiles().length-this.initFileCount) <= 0){
            return true;
        }
        return false;
    };


}
function init($this) {
    /*文件item相关事件开始*/
    var $fileListUl = $this.$uploadMainContainer.find('.filelist' );
    $fileListUl.on('mouseenter','li', function() {
        $(this).find('.file-panel').stop().animate({height: 30});
    });
    //
    $fileListUl.on('mouseleave','li', function() {
        $(this).find('.file-panel').stop().animate({height: 0});
    });
    $fileListUl.on('click', '.file-panel>span.bt', function() {
        var curFileId = $(this).parent().parent().attr('id');
        var files = $this.uploader.getFiles();
        var file;
        for (var i = 0, len = files.length;i < len; i++){
            if (curFileId == files[i].id){
                file = files[i];
                break;
            }
        }
        var $uploadMainContainer = $(this).parent().siblings('p.imgWrap');
        var index = $(this).index(),
            deg;

        switch ( index ) {
            case 0:
                updateFieldVal($this,file);
                $this.uploader.removeFile(file);
                return;

            case 1:
                file.rotation += 90;
                break;

            case 2:
                file.rotation -= 90;
                break;
        }

        if ( supportTransition ) {
            deg = 'rotate(' + file.rotation + 'deg)';
            $uploadMainContainer.css({
                '-webkit-transform': deg,
                '-mos-transform': deg,
                '-o-transform': deg,
                'transform': deg
            });
        } else {
            $uploadMainContainer.css( 'filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation='+ (~~((file.rotation/90)%4 + 4)%4) +')');
        }


    });
    /*文件item相关事件结束*/
    if ($this.skExpand.httpPrefix){
        $this.initList($this.skExpand.httpPrefix);
    }

}

// 当有文件添加进来时执行，负责view的创建
function addFile(file) {

    if ('init'==file.statusText){
        this.uploader.skipFile(file);
    }
    var $this = this;
    var clazz = file.clazz||'';
    var filePath = file.filePath?'name="'+file.filePath+'"':'';
    var $li = $( '<li><span id="' + file.id + '" class="'+clazz+'">' +
        '<p class="title" '+filePath+'>' + file.name + '</p>' +
        '<p class="imgWrap"></p>'+
        '<p class="progress"><span></span></p>' +
        '</span></li>' ),

        $span = $li.find('#' + file.id),$btns;
        if($this.labelType == 0){
            $btns = $('<div class="file-panel">' +
                '<span class="bt cancel">删除</span>' +
                '<span class="bt rotateRight">向右旋转</span>' +
                '<span class="bt rotateLeft">向左旋转</span></div>').appendTo( $span );
        }else{
            $btns = $('<div class="file-panel">' +
                '<span class="bt cancel">删除</span>' +
                '</div>').appendTo( $span );
        }

        var $prgress = $span.find('p.progress span'),
        $uploadMainContainer = $span.find( 'p.imgWrap' ),
        $info = $('<p class="error"></p>'),

        showError = function( code ) {
            switch( code ) {
                case 'exceed_size':
                    text = '文件大小超出';
                    break;

                case 'interrupt':
                    text = '上传暂停';
                    break;

                default:
                    text = '上传失败，请重试';
                    break;
            }

            $info.text( text ).appendTo( $span );
        };

    if ( file.getStatus() === 'invalid' ) {
        showError( file.statusText );
    } else {
        // @todo lazyload
        $uploadMainContainer.text( '预览中' );
        if (this.labelType == 0){//图片
            this.uploader.makeThumb( file, function( error, src ) {
                var img;

                if ( error ) {
                    $uploadMainContainer.text( '不能预览' );
                    return;
                }

                if( isSupportBase64 ) {
                    img = $('<img src="'+src+'">');
                    $uploadMainContainer.empty().append( img );
                } else {
                    $.ajax(basePath +'static/H-ui.admin/lib/webuploader/0.1.5/server/preview.php', {
                        method: 'POST',
                        data: src,
                        dataType:'json'
                    }).done(function( response ) {
                        if (response.result) {
                            img = $('<img src="'+response.result+'">');
                            $uploadMainContainer.empty().append( img );
                        } else {
                            $uploadMainContainer.text("预览出错");
                        }
                    });
                }
            }, this.thumbnailWidth, this.thumbnailHeight );
        }else if (this.labelType == 1){//视频
            this.uploader.makeVideo( file, function( error, src ) {
                if ( error ) {
                    $uploadMainContainer.text( '不能预览' );
                    return;
                }else{
                    // var video='<video  src="' + src + '" controls="controls"></video>'
                    var video = '<video src="' + src + '" width="'+$this.thumbnailWidth+'" ' +
                        'height="'+$this.thumbnailHeight+'" controls="controls">\n' +
                        // '  <source src="' + src + '  type="video/mp4"/>\n' +
                        '你的浏览器不支持video标签' +
                        '</video> ';
                    $uploadMainContainer.empty().append(video);
                    $uploadMainContainer.css("background-color","#000000");
                    $("#yesia_video").on("loadeddata", function (e) {
                        var obj = e.target;
                        var scale = 0.8;
                        var canvas = document.createElement("canvas");
                        canvas.width = obj.videoWidth * scale;
                        canvas.height = obj.videoHeight * scale;
                        canvas.getContext('2d').drawImage(obj, 0, 0, canvas.width, canvas.height);
                        var src= canvas.toDataURL("image/png")
                        var img = $('<img style="width:110px" src="'+src+'">');
                        $uploadMainContainer.empty().append( img );
                        document.getElementById('first_image').value=src

                    } )
                }

            }, this.thumbnailWidth, this.thumbnailHeight );
        }else if (this.labelType == 1){//视频
            this.uploader.makeVideo( file, function( error, src ) {
                if ( error ) {
                    $uploadMainContainer.text( '不能预览' );
                    return;
                }else{
                    // var video='<video  src="' + src + '" controls="controls"></video>'
                    var video = '<video src="' + src + '" width="'+$this.thumbnailWidth+'" ' +
                        'height="'+$this.thumbnailHeight+'" controls="controls">\n' +
                        // '  <source src="' + src + '  type="video/mp4"/>\n' +
                        '你的浏览器不支持video标签' +
                        '</video> ';

                    $uploadMainContainer.empty().append(video);
                    // $uploadMainContainer.css("background-color","#000000");
                    video.onloadeddata = function (e) {
                        console.log('aa');
                        var obj = e.target;
                        var scale = 0.8;
                        var canvas = document.createElement("canvas");
                        canvas.width = obj.videoWidth * scale;
                        canvas.height = obj.videoHeight * scale;
                        canvas.getContext('2d').drawImage(obj, 0, 0, canvas.width, canvas.height);
                        var src= canvas.toDataURL("image/png")
                        var img = $('<img style="width:110px" src="'+src+'">');
                        $uploadMainContainer.empty().append( img );
                        document.getElementById('first_image').value=src

                    };
                }

            }, this.thumbnailWidth, this.thumbnailHeight );
        }else if (this.labelType == 2){//文件
            this.uploader.makeFile( file, function( error, src ) {
                if ( error ) {
                    $uploadMainContainer.text( '不能预览' );
                    return;
                }else{
                    // var video='<video  src="' + src + '" controls="controls"></video>'
                    var a = '<a href="' + src + '"  ' +file.name
                        '</a> ';

                    $uploadMainContainer.empty().append(a);
                }

            }, this.thumbnailWidth, this.thumbnailHeight );
        }


        this.percentages[ file.id ] = [ file.size, 0 ];
        file.rotation = 0;
    }

    file.on('statuschange', function( cur, prev ) {
        if ( prev === 'progress' ) {
            $prgress.hide().width(0);
        } else if ( prev === 'queued' ) {
            //zcp del
            // $li.off( 'mouseenter mouseleave' );
            // $btns.remove();
        }

        // 成功
        if ( cur === 'error' || cur === 'invalid' ) {
            console.log( file.statusText );
            showError( file.statusText );
            $this.percentages[ file.id ][ 1 ] = 1;
        } else if ( cur === 'interrupt' ) {
            showError( 'interrupt' );
        } else if ( cur === 'queued' ) {
            $this.percentages[ file.id ][ 1 ] = 0;
        } else if ( cur === 'progress' ) {
            $info.remove();
            $prgress.css('display', 'block');
        } else if ( cur === 'complete' ) {
            $span.append( '<span class="success"></span>' );
        }

        $span.removeClass( 'state-' + prev ).addClass( 'state-' + cur );
    });
    // $li.on('mouseenter','span', function() {
    //     $btns.stop().animate({height: 30});
    // });
    // //
    // $li.on('mouseleave','span', function() {
    //     $btns.stop().animate({height: 0});
    // });
    //
    // $btns.on( 'click', 'span', function() {
    //     var index = $(this).index(),
    //         deg;
    //
    //     switch ( index ) {
    //         case 0:
    //             updateFieldVal($this,file);
    //             $this.uploader.removeFile(file);
    //             return;
    //
    //         case 1:
    //             file.rotation += 90;
    //             break;
    //
    //         case 2:
    //             file.rotation -= 90;
    //             break;
    //     }
    //
    //     if ( supportTransition ) {
    //         deg = 'rotate(' + file.rotation + 'deg)';
    //         $uploadMainContainer.css({
    //             '-webkit-transform': deg,
    //             '-mos-transform': deg,
    //             '-o-transform': deg,
    //             'transform': deg
    //         });
    //     } else {
    //         $uploadMainContainer.css( 'filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation='+ (~~((file.rotation/90)%4 + 4)%4) +')');
    //     }
    //
    //
    // });

    $li.appendTo( this.$queue );
    var li = $li.get(0);
    li.addEventListener("dragstart", function (e) {
        $btns.stop().animate({height: 0});
        domdrugstart($this,e,this);
    }, false);
    li.addEventListener('dragover', domdrugover, false);
    li.addEventListener('drop', function (e) {
        $btns.stop().animate({height: 0});
        domdrop($this,e,this);
    }, false);
}
//更新字段值
function updateFieldVal($this,file){
    if(!$('#'+file.id).hasClass('state-complete')){
        return
    }
    var newVal = [];
    var vals = $($this.skExpand.fieldId).val().split(';');
    var curName = $('#'+file.id+'>p.title').attr('name');
    for (var i = 0,len = vals.length; i < len; i++){
        console.log(curName)
        if (vals[i].lastIndexOf(curName) == -1){
            newVal.push(vals[i]);
        }
    }
    $this.fileNames.splice($this.fileNames.indexOf(curName),1);
    $($this.skExpand.fieldId).val(newVal.join(';'))
}

// 负责view的销毁
function removeFile(file) {
    var $li = $('#'+file.id).parent();
    // if ('init'==file.statusText){
    //     this.initFileCount--;
    // }

    delete this.percentages[ file.id ];
    this.updateTotalProgress();
    //zcp del
    // $li.off().find('.file-panel').off().end().remove();
    //zcp add
    $li.off().remove();
}
function updateStatus() {
    var text = '', stats;

    if ( this.state === 'ready' ) {
        text = '选中' + this.fileCount + this.labelUnit+this.label+'，共' +
            WebUploader.formatSize( this.fileSize ) + '。';
    } else if ( this.state === 'confirm' ) {
        stats = this.uploader.getStats();
        if ( stats.uploadFailNum ) {
            text = '已成功上传' + (stats.successNum+this.initFileCount)+this.labelUnit+this.label+'至服务器，'+
                stats.uploadFailNum + this.labelUnit+this.label+'上传失败，<a class="retry" href="#">重新上传</a>失败'+this.label+'或<a class="ignore" href="#">忽略</a>'
        }

    } else {
        stats = this.uploader.getStats();
        text = '共' + this.fileCount + this.labelUnit+'（' +
            WebUploader.formatSize( this.fileSize )  +
            '），已上传' + (stats.successNum+this.initFileCount) + this.labelUnit;

        if ( stats.uploadFailNum ) {
            text += '，失败' + stats.uploadFailNum + this.labelUnit;
        }
    }

    this.$info.html( text );
}
function updateTotalProgress() {
    var loaded = 0,
        total = 0,
        spans = this.$progress.children(),
        percent;

    $.each( this.percentages, function( k, v ) {
        total += v[ 0 ];
        loaded += v[ 0 ] * v[ 1 ];
    } );

    percent = total ? loaded / total : 0;


    spans.eq( 0 ).text( Math.round( percent * 100 ) + '%' );
    spans.eq( 1 ).css( 'width', Math.round( percent * 100 ) + '%' );
    this.updateStatus();
}
function dndAccept(items) {
    var denied = false,
        len = items.length,
        i = 0,
        // 修改js类型
        unAllowed = 'text/plain;application/javascript ';

    for ( ; i < len; i++ ) {
        // 如果在列表里面
        if ( ~unAllowed.indexOf( items[ i ].type ) ) {
            denied = true;
            break;
        }
    }

    return !denied;
}


function setState(val) {
    var file, stats;
    if ( val === this.state ) {
        return;
    }
    this.$upload.removeClass( 'state-' + this.state );
    this.$upload.addClass( 'state-' + val );
    this.state = val;

    switch (this.state) {
        case 'pedding':
            this.$placeHolder.removeClass( 'element-invisible' );
            this.$queue.hide();
            this.$statusBar.addClass( 'element-invisible' );
            this.uploader.refresh();
            break;

        case 'ready':
            this.$placeHolder.addClass( 'element-invisible' );
            this.$continueAdd.removeClass( 'element-invisible');
            this.$queue.show();
            this.$statusBar.removeClass('element-invisible');
            this.uploader.refresh();
            break;

        case 'uploading':
            this.$continueAdd.addClass( 'element-invisible' );
            this.$progress.show();
            this.$upload.text( '暂停上传' );
            break;

        case 'paused':
            this.$progress.show();
            this.$upload.text( '继续上传' );
            break;

        case 'confirm':
            this.$progress.hide();
            this.$continueAdd.removeClass( 'element-invisible' );
            this.$upload.text( '开始上传' );

            stats = this.uploader.getStats();
            if ( stats.successNum && !stats.uploadFailNum ) {
                this.setState( 'finish' );
                return;
            }
            break;
        case 'finish':
            stats = this.uploader.getStats();
            if ( stats.successNum ) {
                // alert( '上传成功' );
                // sk.successMsg('上传成功' );
            } else {
                // 没有成功的图片，重设
                this.state = 'done';
                location.reload();
            }
            break;
    }

    this.updateStatus();
}


// var dragEl = null;
/*拖拽相关开始*/
function domdrugstart($wuThis,e,node) {
    // e.target.style.opacity = '0.5';
    // console.log(node)
    $wuThis.dragEl = node;
    e.dataTransfer.effectAllowed = "move";
    e.dataTransfer.setData("text/html", node.innerHTML);
}
function domdrugover(e) {
    if (e.preventDefault) {
        e.preventDefault();
    }
    e.dataTransfer.dropEffect = 'move';
    return false;
}
function domdrop($wuThis,e,node) {
    if (e.stopPropagation) {
        e.stopPropagation();
    }
    // console.log()
    // 位置互换
    if ($wuThis.dragEl != node) {
        $wuThis.dragEl.innerHTML = node.innerHTML;
        node.innerHTML = e.dataTransfer.getData('text/html');

        //更新表单域
        var $lis = $(node.parentNode).find('li');
        // console.log($lis.length)
        var fileNames = []
        for (var i = 0, len = $lis.length; i < len; i++){
            var name = $lis.eq(i).find('.state-complete p.title').attr('name');
            if (name){
                fileNames.push(name);
            }
        }
        $wuThis.fileNames = fileNames;
        $($wuThis.skExpand.fieldId).val(fileNames.join(';'));
    }
    // return false;
}
/*拖拽相关结束*/

/*初始加载文件相关开始*/
function getFileBlob (url, cb) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", url);
    xhr.responseType = "blob";
    xhr.addEventListener('load', function() {
        cb(xhr.response);
    });
    xhr.send();
}
function blobToFile(blob, name) {
    blob.lastModifiedDate = new Date();
    blob.name = name;
    return blob;
}
function getFileObject(filePathOrUrl, cb) {
    getFileBlob(filePathOrUrl, function (blob) {
        var fileName = filePathOrUrl.substring((filePathOrUrl.lastIndexOf('/')+1));
        cb(blobToFile(blob, fileName));
    });
};

//初始化显示的列表
// var picList = ['图片url','图片url','图片url','图片url' ]
function initList(imgHttpPrefix) {
    var $this = this;
    var fileList = $(this.skExpand.fieldId).val().split(';');
    var fileLi = {};
    // var fileCount = 0;
    // var fileSize = 0;
    $.each(fileList, function(index,item){
        if (!item) return;
        // fileCount++;
        $this.fileNames.push(item);
        console.log(item);
        var $fileListUl = $this.$uploadMainContainer.find('.queueList ul.filelist');
        getFileObject(imgHttpPrefix+item, function(fileObject) {
            var wuFile = new WebUploader.Lib.File(WebUploader.guid('rt_'),fileObject);
            var file = new WebUploader.File(wuFile);
            console.log(file);
            file.clazz = 'state-complete';
            file.statusText = 'init';
            file.filePath = item;
            $this.initFileCount++;
            // $this.fileCount++;
            // fileSize += file.size;
            fileLi[item] = file;

            //最后一个
            if (fileList.length > 1 && fileList.length == index+1){
                $.each($this.fileNames, function(index2,item2){
                    $this.uploader.addFiles(fileLi[item2]);
                });
            }else if (fileList.length == 1){
                $this.uploader.addFiles(file);
            }
        })
    });
    // this.fileCount = fileCount;
    // this.fileSize = fileSize;
    //从队列初始初始文件
}
/*初始加载文件相关结束*/