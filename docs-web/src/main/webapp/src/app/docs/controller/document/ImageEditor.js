angular.module('docs').controller('ImageEditor', function (
    $scope, $uibModalInstance, file, $timeout
) {
    $scope.file = file;
    $scope.cropper = null;

    // 初始化 cropper
    function initCropper() {
        var img = document.getElementById('image-to-edit');
        if (!img) {
            console.error('找不到 <img> 元素');
            return;
        }

        function buildCropper() {
            if ($scope.cropper) return; // 防重复初始化
            $scope.cropper = new Cropper(img, {
                viewMode: 1,
                autoCropArea: 1
            });
            console.log('Cropper 初始化完成');
        }

        img.onload = buildCropper;
        img.onerror = function () {
            console.error('图片加载失败');
        };

        if (img.complete) {
            buildCropper();
        }
    }

    // 等待 DOM 渲染完成再初始化
    $timeout(initCropper);

    // 保存裁剪后的图片
    $scope.save = function () {
        if (!$scope.cropper) return;

        $scope.cropper.getCroppedCanvas().toBlob(function (blob) {
            var editedFile = new File([blob], $scope.file.name, {
                type: $scope.file.mimetype,
                lastModified: Date.now()
            });
            $uibModalInstance.close(editedFile);
        }, $scope.file.mimetype);
    };

    $scope.rotate = function (deg) {
        if ($scope.cropper) {
            $scope.cropper.rotate(deg);
        }
    };

    $scope.flipX = function () {
        if ($scope.cropper) {
            var scaleX = $scope.cropper.getData().scaleX || 1;
            $scope.cropper.scaleX(-scaleX);
        }
    };

    $scope.flipY = function () {
        if ($scope.cropper) {
            var scaleY = $scope.cropper.getData().scaleY || 1;
            $scope.cropper.scaleY(-scaleY);
        }
    };

    $scope.reset = function () {
        if ($scope.cropper) {
            $scope.cropper.reset();
        }
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    };
});
