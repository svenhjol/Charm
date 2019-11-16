<?php if (!defined('LAUNCHED')) die(); ?>

<div class="container charm-category charm-category-<?php echo $category->getId(); ?>">

<?php

$features = Feature::getFeatures($category->getId());

foreach ($features as $id => $feature) {

?>

<div class="card charm-feature">
    <div class="row no-gutters">
        <div class="col-md-4 col-sm-5"><img src="images/features/card_<?php echo $feature->getId(); ?>.png" class="card-img" alt="Image for <?php echo $feature->getTitle(); ?>"></div>
        <div class="col-md-8 col-sm-7">
            <div class="card-body m-4">
                <h3 class="card-title"><?php echo $feature->getTitle(); ?></h5>
                <p class="card-text"><?php echo $feature->getTeaser(); ?></p>
                <?php if ($feature->hasBody()) { ?><button type="button" class="btn btn-info" data-toggle="modal" data-target="#<?php echo $feature->getId(); ?>"><span class="fa fa-book-open"></span>&nbsp;&nbsp;Read more</a><?php } ?>
            </div>
        </div>
    </div>
</div>

<?php if ($feature->hasBody()) { ?>
<div class="modal fade" id="<?php echo $feature->getId(); ?>" tabindex="-1" role="dialog" aria-labelledby="<?php echo $feature->getId(); ?>Label" aria-hidden="true">
<div class="modal-dialog modal-dialog-centered" role="document"><div class="modal-content">
    <div class="modal-header">
        <h3 class="modal-title" id="<?php echo $feature->getId(); ?>Label"><?php echo $feature->getTitle(); ?></h3>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><span class="fa fa-times"></span></button>
    </div>
    <div class="modal-body"><?php echo $feature->getBody(); ?></div>
</div></div>
</div>
<?php } ?>

<?php

}

?>

</div>