// ImageEditor.js
angular.module('docs').controller('ImageEditor', function (
    $scope, $uibModalInstance, file, $timeout
) {
    $scope.file = file;
    $scope.cropper = null;

    // 初始化 cropper
    const initCropper = () => {
        const img = document.getElementById('image-to-edit');
        if (!img) {
            console.error('找不到 <img> 元素');
            return;
        }

        const buildCropper = () => {
            if ($scope.cropper) return; // 防重复初始化
            $scope.cropper = new Cropper(img, {
                viewMode: 1,
                autoCropArea: 1,
            });
            console.log('Cropper 初始化完成');
        };

        img.onload = buildCropper;
        img.onerror = () => console.error('图片加载失败');

        if (img.complete) {
            buildCropper();
        }
    };

    // 等待 DOM 渲染完成再初始化
    $timeout(initCropper);

    // 保存裁剪后的图片
    $scope.save = function () {
        if (!$scope.cropper) return;

        $scope.cropper.getCroppedCanvas().toBlob(blob => {
            const editedFile = new File([blob], $scope.file.name, {
                type: $scope.file.mimetype,
                lastModified: Date.now(),
            });
            $uibModalInstance.close(editedFile);
        }, $scope.file.mimetype);
    };

    // 工具方法
    $scope.rotate = deg => $scope.cropper?.rotate(deg);

    $scope.flipX = () => {
        if ($scope.cropper) {
            const scaleX = $scope.cropper.getData().scaleX || 1;
            $scope.cropper.scaleX(-scaleX);
        }
    };

    $scope.flipY = () => {
        if ($scope.cropper) {
            const scaleY = $scope.cropper.getData().scaleY || 1;
            $scope.cropper.scaleY(-scaleY);
        }
    };

    $scope.reset = () => $scope.cropper?.reset();

    $scope.cancel = () => $uibModalInstance.dismiss();
});
