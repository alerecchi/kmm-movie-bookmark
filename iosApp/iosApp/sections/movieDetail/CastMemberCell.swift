//
//  CastMemberCell.swift
//  iosApp
//
//  Created by feanor on 04/01/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import UIKit
import Kingfisher

final class CastMemberCell: UICollectionViewCell {

	@IBOutlet weak var castMemberImage: UIImageView!
	@IBOutlet weak var castMemberNameLabel: UILabel!

	func configure(viewModel: CastMemberViewModel) {
		if let profilePath = viewModel.profilePath, !profilePath.isEmpty {
			castMemberImage.kf.indicatorType = .activity
			castMemberImage.kf.setImage(
				with: URL(string: profilePath)!,
				options: [
					.processor(DownsamplingImageProcessor(size: castMemberImage.bounds.size)),
					.scaleFactor(UIScreen.main.scale),
					.cacheOriginalImage
				]
			)
		}

		castMemberNameLabel.text = viewModel.name
	}

	override func prepareForReuse() {
		super.prepareForReuse()
		castMemberImage.kf.cancelDownloadTask()
		castMemberNameLabel.text = nil
	}

}
